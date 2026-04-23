package quickbit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickbit.core.model.CurrencyIndicatorsModel;
import quickbit.core.model.IndicatorLatestModel;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.CurrencyPrice;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CurrencyIndicatorServiceImpl implements CurrencyIndicatorService {
    private static final int MA_PERIOD = 20;
    private static final int EMA_PERIOD = 20;
    private static final int BOLL_PERIOD = 20;
    private static final int RSI_PERIOD = 14;
    private static final int WR_PERIOD = 14;
    private static final int KDJ_PERIOD = 9;
    private static final int STOCH_RSI_PERIOD = 14;
    private static final int STOCH_RSI_SMOOTH_K = 3;
    private static final int STOCH_RSI_SMOOTH_D = 3;
    private static final int MACD_FAST = 12;
    private static final int MACD_SLOW = 26;
    private static final int MACD_SIGNAL = 9;

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyIndicatorServiceImpl(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public CurrencyIndicatorsModel calculateForCurrency(Currency currency) {
        if (currency == null || currency.getId() == null) {
            return new CurrencyIndicatorsModel().setLatest(new IndicatorLatestModel());
        }

        Set<CurrencyPrice> rawPrices = currencyService.getAllPrices(currency.getId());
        List<CurrencyPrice> orderedPrices = rawPrices == null
            ? Collections.emptyList()
            : rawPrices
            .stream()
            .filter(Objects::nonNull)
            .filter(price -> price.getCreatedAt() != null && price.getPrice() != null)
            .sorted(Comparator.comparing(CurrencyPrice::getCreatedAt))
            .collect(Collectors.toList());

        if (orderedPrices.isEmpty()) {
            return new CurrencyIndicatorsModel().setLatest(new IndicatorLatestModel()
                .setBoll(Collections.emptyList())
                .setMacd(Collections.emptyList())
                .setKdj(Collections.emptyList())
                .setStochRsi(Collections.emptyList()));
        }

        List<String> labels = orderedPrices.stream()
            .map(price -> price.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .collect(Collectors.toList());
        List<Double> closePrices = orderedPrices.stream()
            .map(price -> price.getPrice().doubleValue())
            .collect(Collectors.toList());
        List<Double> volumes = orderedPrices.stream()
            .map(price -> Objects.nonNull(price.getVolume24h()) ? price.getVolume24h().doubleValue() : 0D)
            .collect(Collectors.toList());

        int maPeriod = clampPeriod(closePrices.size(), MA_PERIOD, 2);
        int emaPeriod = clampPeriod(closePrices.size(), EMA_PERIOD, 2);
        int bollPeriod = clampPeriod(closePrices.size(), BOLL_PERIOD, 2);
        int rsiPeriod = clampPeriod(closePrices.size(), RSI_PERIOD, 2);
        int wrPeriod = clampPeriod(closePrices.size(), WR_PERIOD, 2);
        int kdjPeriod = clampPeriod(closePrices.size(), KDJ_PERIOD, 2);
        int mavolPeriod = clampPeriod(volumes.size(), MA_PERIOD, 2);
        int stochRsiPeriod = clampPeriod(closePrices.size(), STOCH_RSI_PERIOD, 2);

        List<Double> ma = calcSma(closePrices, maPeriod);
        List<Double> ema = calcEma(closePrices, emaPeriod);
        BollResult boll = calcBoll(closePrices, bollPeriod, 2D);
        List<Double> sar = calcParabolicSar(closePrices, closePrices, 0.02, 0.2);
        List<Double> mavol = calcSma(volumes, mavolPeriod);
        MacdResult macd = calcMacd(closePrices, MACD_FAST, MACD_SLOW, MACD_SIGNAL);
        KdjResult kdj = calcKdj(closePrices, closePrices, closePrices, kdjPeriod);
        List<Double> rsi = calcRsi(closePrices, rsiPeriod);
        List<Double> wr = calcWilliamsR(closePrices, closePrices, closePrices, wrPeriod);
        StochRsiResult stochRsi = calcStochRsi(rsi, stochRsiPeriod, STOCH_RSI_SMOOTH_K, STOCH_RSI_SMOOTH_D);

        IndicatorLatestModel latest = new IndicatorLatestModel()
            .setMa(lastDefined(ma))
            .setEma(lastDefined(ema))
            .setBoll(nullableTriplet(lastDefined(boll.lower), lastDefined(boll.middle), lastDefined(boll.upper)))
            .setSar(lastDefined(sar))
            .setMavol(lastDefined(mavol))
            .setMacd(nullableTriplet(lastDefined(macd.macd), lastDefined(macd.signal), lastDefined(macd.histogram)))
            .setKdj(nullableTriplet(lastDefined(kdj.k), lastDefined(kdj.d), lastDefined(kdj.j)))
            .setRsi(lastDefined(rsi))
            .setWr(lastDefined(wr))
            .setStochRsi(nullablePair(lastDefined(stochRsi.k), lastDefined(stochRsi.d)));

        return new CurrencyIndicatorsModel()
            .setLabels(labels)
            .setClosePrices(closePrices)
            .setMa(ma)
            .setEma(ema)
            .setBollUpper(boll.upper)
            .setBollMiddle(boll.middle)
            .setBollLower(boll.lower)
            .setSar(sar)
            .setMavol(mavol)
            .setMacd(macd.macd)
            .setMacdSignal(macd.signal)
            .setMacdHistogram(macd.histogram)
            .setKdjK(kdj.k)
            .setKdjD(kdj.d)
            .setKdjJ(kdj.j)
            .setRsi(rsi)
            .setWr(wr)
            .setStochRsiK(stochRsi.k)
            .setStochRsiD(stochRsi.d)
            .setLatest(latest);
    }

    private int clampPeriod(int length, int preferred, int min) {
        if (length <= 0) {
            return preferred;
        }
        return Math.max(min, Math.min(preferred, length));
    }

    private List<Double> calcSma(List<Double> values, int period) {
        List<Double> out = createNullList(values.size());
        for (int i = period - 1; i < values.size(); i++) {
            double sum = 0;
            for (int j = i - period + 1; j <= i; j++) {
                sum += values.get(j);
            }
            out.set(i, sum / period);
        }
        return out;
    }

    private List<Double> calcEma(List<Double> values, int period) {
        List<Double> out = createNullList(values.size());
        if (values.size() < period) {
            return out;
        }
        double alpha = 2D / (period + 1);
        double seed = 0;
        for (int i = 0; i < period; i++) {
            seed += values.get(i);
        }
        out.set(period - 1, seed / period);
        for (int i = period; i < values.size(); i++) {
            out.set(i, alpha * values.get(i) + (1 - alpha) * out.get(i - 1));
        }
        return out;
    }

    private BollResult calcBoll(List<Double> values, int period, double k) {
        List<Double> middle = calcSma(values, period);
        List<Double> upper = createNullList(values.size());
        List<Double> lower = createNullList(values.size());
        for (int i = period - 1; i < values.size(); i++) {
            double std = calcStd(values, i - period + 1, i);
            upper.set(i, middle.get(i) + k * std);
            lower.set(i, middle.get(i) - k * std);
        }
        return new BollResult(upper, middle, lower);
    }

    private double calcStd(List<Double> values, int start, int end) {
        int size = end - start + 1;
        if (size <= 1) {
            return 0;
        }
        double mean = 0;
        for (int i = start; i <= end; i++) {
            mean += values.get(i);
        }
        mean /= size;
        double variance = 0;
        for (int i = start; i <= end; i++) {
            variance += Math.pow(values.get(i) - mean, 2);
        }
        return Math.sqrt(variance / size);
    }

    private List<Double> calcParabolicSar(List<Double> highs, List<Double> lows, double step, double maxStep) {
        List<Double> out = createNullList(highs.size());
        if (highs.size() < 2) {
            return out;
        }
        boolean upTrend = highs.get(1) >= highs.get(0);
        double af = step;
        double ep = upTrend ? highs.get(1) : lows.get(1);
        double sar = upTrend ? lows.get(0) : highs.get(0);
        out.set(1, sar);

        for (int i = 2; i < highs.size(); i++) {
            sar = sar + af * (ep - sar);
            if (upTrend) {
                if (lows.get(i) < sar) {
                    upTrend = false;
                    sar = ep;
                    ep = lows.get(i);
                    af = step;
                } else if (highs.get(i) > ep) {
                    ep = highs.get(i);
                    af = Math.min(af + step, maxStep);
                }
            } else if (highs.get(i) > sar) {
                upTrend = true;
                sar = ep;
                ep = highs.get(i);
                af = step;
            } else if (lows.get(i) < ep) {
                ep = lows.get(i);
                af = Math.min(af + step, maxStep);
            }
            out.set(i, sar);
        }
        return out;
    }

    private MacdResult calcMacd(List<Double> values, int shortPeriod, int longPeriod, int signalPeriod) {
        List<Double> shortEma = calcEma(values, shortPeriod);
        List<Double> longEma = calcEma(values, longPeriod);
        List<Double> macd = createNullList(values.size());
        for (int i = 0; i < values.size(); i++) {
            if (shortEma.get(i) != null && longEma.get(i) != null) {
                macd.set(i, shortEma.get(i) - longEma.get(i));
            }
        }
        List<Double> signal = calcEmaNullable(macd, signalPeriod);
        List<Double> histogram = createNullList(values.size());
        for (int i = 0; i < values.size(); i++) {
            if (macd.get(i) != null && signal.get(i) != null) {
                histogram.set(i, macd.get(i) - signal.get(i));
            }
        }
        return new MacdResult(macd, signal, histogram);
    }

    private List<Double> calcEmaNullable(List<Double> values, int period) {
        List<Double> out = createNullList(values.size());
        List<Integer> validIdx = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) != null) {
                validIdx.add(i);
            }
        }
        if (validIdx.size() < period) {
            return out;
        }
        double alpha = 2D / (period + 1);
        double sum = 0;
        for (int i = 0; i < period; i++) {
            sum += values.get(validIdx.get(i));
        }
        out.set(validIdx.get(period - 1), sum / period);
        for (int i = period; i < validIdx.size(); i++) {
            int prevIndex = validIdx.get(i - 1);
            int index = validIdx.get(i);
            double prev = out.get(prevIndex) != null ? out.get(prevIndex) : values.get(prevIndex);
            out.set(index, alpha * values.get(index) + (1 - alpha) * prev);
        }
        return out;
    }

    private KdjResult calcKdj(List<Double> highs, List<Double> lows, List<Double> closes, int period) {
        List<Double> k = createNullList(closes.size());
        List<Double> d = createNullList(closes.size());
        List<Double> j = createNullList(closes.size());
        double prevK = 50;
        double prevD = 50;
        for (int i = 0; i < closes.size(); i++) {
            if (i < period - 1) {
                continue;
            }
            double hh = max(highs, i - period + 1, i);
            double ll = min(lows, i - period + 1, i);
            double rsv = hh == ll ? 50 : ((closes.get(i) - ll) / (hh - ll)) * 100;
            prevK = (2D / 3) * prevK + (1D / 3) * rsv;
            prevD = (2D / 3) * prevD + (1D / 3) * prevK;
            k.set(i, prevK);
            d.set(i, prevD);
            j.set(i, 3 * prevK - 2 * prevD);
        }
        return new KdjResult(k, d, j);
    }

    private List<Double> calcRsi(List<Double> values, int period) {
        List<Double> out = createNullList(values.size());
        if (values.size() <= period) {
            return out;
        }
        double gain = 0;
        double loss = 0;
        for (int i = 1; i <= period; i++) {
            double delta = values.get(i) - values.get(i - 1);
            gain += Math.max(delta, 0);
            loss += Math.max(-delta, 0);
        }
        double avgGain = gain / period;
        double avgLoss = loss / period;
        out.set(period, calcRsiValue(avgGain, avgLoss));
        for (int i = period + 1; i < values.size(); i++) {
            double delta = values.get(i) - values.get(i - 1);
            double up = Math.max(delta, 0);
            double down = Math.max(-delta, 0);
            avgGain = ((period - 1) * avgGain + up) / period;
            avgLoss = ((period - 1) * avgLoss + down) / period;
            out.set(i, calcRsiValue(avgGain, avgLoss));
        }
        return out;
    }

    private double calcRsiValue(double avgGain, double avgLoss) {
        if (avgLoss == 0) {
            return 100;
        }
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    private List<Double> calcWilliamsR(List<Double> highs, List<Double> lows, List<Double> closes, int period) {
        List<Double> out = createNullList(closes.size());
        for (int i = period - 1; i < closes.size(); i++) {
            double hh = max(highs, i - period + 1, i);
            double ll = min(lows, i - period + 1, i);
            out.set(i, hh == ll ? -50 : ((hh - closes.get(i)) / (hh - ll)) * -100);
        }
        return out;
    }

    private StochRsiResult calcStochRsi(List<Double> rsi, int period, int smoothK, int smoothD) {
        List<Double> raw = createNullList(rsi.size());
        for (int i = period - 1; i < rsi.size(); i++) {
            List<Double> window = rsi.subList(i - period + 1, i + 1)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            if (window.size() < period || rsi.get(i) == null) {
                continue;
            }
            double max = window.stream().max(Double::compareTo).orElse(0D);
            double min = window.stream().min(Double::compareTo).orElse(0D);
            raw.set(i, max == min ? 0.5 : (rsi.get(i) - min) / (max - min));
        }
        List<Double> k = calcSmaNullable(raw, smoothK);
        List<Double> d = calcSmaNullable(k, smoothD);
        return new StochRsiResult(k, d);
    }

    private List<Double> calcSmaNullable(List<Double> values, int period) {
        List<Double> out = createNullList(values.size());
        for (int i = period - 1; i < values.size(); i++) {
            List<Double> window = values.subList(i - period + 1, i + 1);
            if (window.stream().anyMatch(Objects::isNull)) {
                continue;
            }
            double sum = window.stream().mapToDouble(Double::doubleValue).sum();
            out.set(i, sum / period);
        }
        return out;
    }

    private Double lastDefined(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        for (int i = values.size() - 1; i >= 0; i--) {
            Double value = values.get(i);
            if (value != null && !value.isNaN() && !value.isInfinite()) {
                return value;
            }
        }
        return null;
    }

    private List<Double> nullablePair(Double first, Double second) {
        List<Double> values = new ArrayList<>(2);
        values.add(first);
        values.add(second);
        return values;
    }

    private List<Double> nullableTriplet(Double first, Double second, Double third) {
        List<Double> values = new ArrayList<>(3);
        values.add(first);
        values.add(second);
        values.add(third);
        return values;
    }

    private double max(List<Double> values, int from, int to) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = from; i <= to; i++) {
            max = Math.max(max, values.get(i));
        }
        return max;
    }

    private double min(List<Double> values, int from, int to) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = from; i <= to; i++) {
            min = Math.min(min, values.get(i));
        }
        return min;
    }

    private List<Double> createNullList(int size) {
        List<Double> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(null);
        }
        return list;
    }

    private static class BollResult {
        private final List<Double> upper;
        private final List<Double> middle;
        private final List<Double> lower;

        private BollResult(List<Double> upper, List<Double> middle, List<Double> lower) {
            this.upper = upper;
            this.middle = middle;
            this.lower = lower;
        }
    }

    private static class MacdResult {
        private final List<Double> macd;
        private final List<Double> signal;
        private final List<Double> histogram;

        private MacdResult(List<Double> macd, List<Double> signal, List<Double> histogram) {
            this.macd = macd;
            this.signal = signal;
            this.histogram = histogram;
        }
    }

    private static class KdjResult {
        private final List<Double> k;
        private final List<Double> d;
        private final List<Double> j;

        private KdjResult(List<Double> k, List<Double> d, List<Double> j) {
            this.k = k;
            this.d = d;
            this.j = j;
        }
    }

    private static class StochRsiResult {
        private final List<Double> k;
        private final List<Double> d;

        private StochRsiResult(List<Double> k, List<Double> d) {
            this.k = k;
            this.d = d;
        }
    }
}
