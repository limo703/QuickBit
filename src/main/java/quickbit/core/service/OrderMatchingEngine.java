package quickbit.core.service;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import quickbit.core.form.CreateTransactionForm;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Transaction;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import quickbit.dbcore.repositories.WalletRepository;
import quickbit.core.util.MoneyValidationUtil;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderMatchingEngine {

    private final CurrencyService currencyService;
    private final TransactionService transactionService;
    private final WalletService walletService;
    private final WalletRepository walletRepository;

    @Autowired
    public OrderMatchingEngine(
        CurrencyService currencyService,
        TransactionService transactionService,
        @Lazy WalletService walletService,
        WalletRepository walletRepository
    ) {
        this.currencyService = currencyService;
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void process(@NotNull CreateTransactionForm form, @NotNull User taker) {
        validateOrder(form);

        Currency currency = currencyService.getByName(form.getCurrencyName());
        BigDecimal orderAmount = BigDecimal.valueOf(form.getAmount());
        BigDecimal orderPrice = BigDecimal.valueOf(form.getPrice());
        boolean takerBuy = form.getTypeOpp();

        lockMatchingScope(taker.getId(), currency.getId());
        reserveOrderFunds(taker.getId(), currency.getId(), orderAmount, orderPrice, takerBuy);

        List<Transaction> makers = transactionService.findMatchingOrders(
            currency.getId(),
            takerBuy,
            form.getPrice()
        );

        BigDecimal remainingAmount = orderAmount;
        Set<Wallet> walletsToSave = new HashSet<>();
        Set<Transaction> makersToDelete = new HashSet<>();

        for (Transaction maker : makers) {
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            if (Objects.equals(maker.getUserId(), taker.getId())) {
                continue;
            }

            BigDecimal makerAmount = BigDecimal.valueOf(maker.getAmount());
            BigDecimal tradeAmount = remainingAmount.min(makerAmount);
            BigDecimal tradePrice = BigDecimal.valueOf(maker.getOperationPrice());

            applyTrade(
                taker.getId(),
                currency.getId(),
                takerBuy,
                orderPrice,
                tradeAmount,
                tradePrice,
                walletsToSave
            );

            applyTrade(
                maker.getUserId(),
                currency.getId(),
                maker.getTypeOpp(),
                tradePrice,
                tradeAmount,
                tradePrice,
                walletsToSave
            );

            BigDecimal makerRemainder = makerAmount.subtract(tradeAmount);
            if (makerRemainder.compareTo(BigDecimal.ZERO) == 0) {
                makersToDelete.add(maker);
            } else {
                maker.setAmount(makerRemainder.doubleValue());
                transactionService.save(maker);
            }

            remainingAmount = remainingAmount.subtract(tradeAmount);
        }

        if (!makersToDelete.isEmpty()) {
            transactionService.removeAll(makersToDelete);
        }

        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            transactionService.save(
                new Transaction()
                    .setAmount(remainingAmount.doubleValue())
                    .setOperationPrice(form.getPrice())
                    .setTypeOpp(takerBuy)
                    .setCurrencyId(currency.getId())
                    .setUser(taker)
            );
        }

        walletRepository.saveAll(walletsToSave);
    }

    private void validateOrder(CreateTransactionForm form) {
        if (form.getTypeOpp() == null
            || form.getAmount() == null
            || form.getPrice() == null
            || form.getCurrencyName() == null
            || form.getCurrencyName().isBlank()) {
            throw new IllegalArgumentException("Order is incomplete");
        }
        if (!MoneyValidationUtil.isPositiveFinite(form.getAmount())
            || !MoneyValidationUtil.isPositiveFinite(form.getPrice())) {
            throw new IllegalArgumentException("Order amount and price must be positive finite numbers");
        }
    }

    private void lockMatchingScope(Long userId, Long currencyId) {
        List<String> lockKeys = new ArrayList<>();
        lockKeys.add("orderbook:currency:" + currencyId);
        lockKeys.add("wallet:user:" + userId);
        Collections.sort(lockKeys);

        for (String lockKey : lockKeys) {
            transactionService.lockTransactionByKey(lockKey);
        }
    }

    private void reserveOrderFunds(
        Long userId,
        Long currencyId,
        BigDecimal amount,
        BigDecimal limitPrice,
        boolean buyOrder
    ) {
        Wallet reserveWallet = buyOrder
            ? walletService.getDefault(userId)
            : walletService.getWalletByUserIdAndCurrencyId(userId, currencyId);

        BigDecimal reservedValue = buyOrder ? amount.multiply(limitPrice) : amount;
        reserveWallet.setAmount(MoneyValidationUtil.nonNull(reserveWallet.getAmount()));
        reserveWallet.setReservedAmount(MoneyValidationUtil.nonNull(reserveWallet.getReservedAmount()));
        BigDecimal available = reserveWallet.getAmount();
        if (available.compareTo(reservedValue) < 0) {
            throw new IllegalStateException("Insufficient available balance for order reservation");
        }
        reserveWallet.subtract(reservedValue);
        reserveWallet.addReserved(reservedValue);
        walletRepository.save(reserveWallet);
    }

    private void applyTrade(
        Long userId,
        Long currencyId,
        boolean buyOrder,
        BigDecimal limitPrice,
        BigDecimal tradeAmount,
        BigDecimal tradePrice,
        Set<Wallet> walletsToSave
    ) {
        Wallet baseWallet = walletService.getWalletByUserIdAndCurrencyId(userId, currencyId);
        Wallet quoteWallet = walletService.getDefault(userId);
        baseWallet.setAmount(MoneyValidationUtil.nonNull(baseWallet.getAmount()));
        baseWallet.setReservedAmount(MoneyValidationUtil.nonNull(baseWallet.getReservedAmount()));
        quoteWallet.setAmount(MoneyValidationUtil.nonNull(quoteWallet.getAmount()));
        quoteWallet.setReservedAmount(MoneyValidationUtil.nonNull(quoteWallet.getReservedAmount()));

        if (buyOrder) {
            BigDecimal lockedQuote = tradeAmount.multiply(limitPrice);
            BigDecimal executionCost = tradeAmount.multiply(tradePrice);
            BigDecimal refund = lockedQuote.subtract(executionCost);

            quoteWallet.subtractReserved(lockedQuote);
            if (refund.compareTo(BigDecimal.ZERO) > 0) {
                quoteWallet.add(refund);
            }
            baseWallet.add(tradeAmount);
        } else {
            BigDecimal executionIncome = tradeAmount.multiply(tradePrice);
            baseWallet.subtractReserved(tradeAmount);
            quoteWallet.add(executionIncome);
        }

        walletsToSave.add(baseWallet);
        walletsToSave.add(quoteWallet);
    }
}
