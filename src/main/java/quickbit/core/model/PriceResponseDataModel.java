package quickbit.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceResponseDataModel {
    private Map<String, CurrencyInfo> data;

    public Map<String, CurrencyInfo> getData() {
        return data;
    }

    public void setData(Map<String, CurrencyInfo> data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrencyInfo {
        private int id;
        private String name;
        private String symbol;
        private String slug;
        private int is_active;
        private int is_fiat;
        private BigDecimal circulating_supply;
        private BigDecimal total_supply;
        private BigDecimal max_supply;
        private String date_added;
        private int num_market_pairs;
        private int cmc_rank;
        private String last_updated;
        private String[] tags;
        private Object platform; // Можем оставить как Object, если поле platform приходит null
        private BigDecimal self_reported_circulating_supply;
        private BigDecimal self_reported_market_cap;
        private Map<String, Quote> quote;

        // Геттеры и сеттеры для всех полей

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public int getIs_active() {
            return is_active;
        }

        public void setIs_active(int is_active) {
            this.is_active = is_active;
        }

        public int getIs_fiat() {
            return is_fiat;
        }

        public void setIs_fiat(int is_fiat) {
            this.is_fiat = is_fiat;
        }

        public BigDecimal getCirculating_supply() {
            return circulating_supply;
        }

        public void setCirculating_supply(BigDecimal circulating_supply) {
            this.circulating_supply = circulating_supply;
        }

        public BigDecimal getTotal_supply() {
            return total_supply;
        }

        public void setTotal_supply(BigDecimal total_supply) {
            this.total_supply = total_supply;
        }

        public BigDecimal getMax_supply() {
            return max_supply;
        }

        public void setMax_supply(BigDecimal max_supply) {
            this.max_supply = max_supply;
        }

        public String getDate_added() {
            return date_added;
        }

        public void setDate_added(String date_added) {
            this.date_added = date_added;
        }

        public int getNum_market_pairs() {
            return num_market_pairs;
        }

        public void setNum_market_pairs(int num_market_pairs) {
            this.num_market_pairs = num_market_pairs;
        }

        public int getCmc_rank() {
            return cmc_rank;
        }

        public void setCmc_rank(int cmc_rank) {
            this.cmc_rank = cmc_rank;
        }

        public String getLast_updated() {
            return last_updated;
        }

        public void setLast_updated(String last_updated) {
            this.last_updated = last_updated;
        }

        public String[] getTags() {
            return tags;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }

        public Object getPlatform() {
            return platform;
        }

        public void setPlatform(Object platform) {
            this.platform = platform;
        }

        public BigDecimal getSelf_reported_circulating_supply() {
            return self_reported_circulating_supply;
        }

        public void setSelf_reported_circulating_supply(BigDecimal self_reported_circulating_supply) {
            this.self_reported_circulating_supply = self_reported_circulating_supply;
        }

        public BigDecimal getSelf_reported_market_cap() {
            return self_reported_market_cap;
        }

        public void setSelf_reported_market_cap(BigDecimal self_reported_market_cap) {
            this.self_reported_market_cap = self_reported_market_cap;
        }

        public Map<String, Quote> getQuote() {
            return quote;
        }

        public void setQuote(Map<String, Quote> quote) {
            this.quote = quote;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Quote {
            private BigDecimal price;
            private BigDecimal volume_24h;
            private BigDecimal volume_change_24h;
            private BigDecimal percent_change_1h;
            private BigDecimal percent_change_24h;
            private BigDecimal percent_change_7d;
            private BigDecimal percent_change_30d;
            private BigDecimal market_cap;
            private BigDecimal market_cap_dominance;
            private BigDecimal fully_diluted_market_cap;
            private String last_updated;

            // Геттеры и сеттеры для всех полей

            public BigDecimal getPrice() {
                return price;
            }

            public void setPrice(BigDecimal price) {
                this.price = price;
            }

            public BigDecimal getVolume_24h() {
                return volume_24h;
            }

            public void setVolume_24h(BigDecimal volume_24h) {
                this.volume_24h = volume_24h;
            }

            public BigDecimal getVolume_change_24h() {
                return volume_change_24h;
            }

            public void setVolume_change_24h(BigDecimal volume_change_24h) {
                this.volume_change_24h = volume_change_24h;
            }

            public BigDecimal getPercent_change_1h() {
                return percent_change_1h;
            }

            public void setPercent_change_1h(BigDecimal percent_change_1h) {
                this.percent_change_1h = percent_change_1h;
            }

            public BigDecimal getPercent_change_24h() {
                return percent_change_24h;
            }

            public void setPercent_change_24h(BigDecimal percent_change_24h) {
                this.percent_change_24h = percent_change_24h;
            }

            public BigDecimal getPercent_change_7d() {
                return percent_change_7d;
            }

            public void setPercent_change_7d(BigDecimal percent_change_7d) {
                this.percent_change_7d = percent_change_7d;
            }

            public BigDecimal getPercent_change_30d() {
                return percent_change_30d;
            }

            public void setPercent_change_30d(BigDecimal percent_change_30d) {
                this.percent_change_30d = percent_change_30d;
            }

            public BigDecimal getMarket_cap() {
                return market_cap;
            }

            public void setMarket_cap(BigDecimal market_cap) {
                this.market_cap = market_cap;
            }

            public BigDecimal getMarket_cap_dominance() {
                return market_cap_dominance;
            }

            public void setMarket_cap_dominance(BigDecimal market_cap_dominance) {
                this.market_cap_dominance = market_cap_dominance;
            }

            public BigDecimal getFully_diluted_market_cap() {
                return fully_diluted_market_cap;
            }

            public void setFully_diluted_market_cap(BigDecimal fully_diluted_market_cap) {
                this.fully_diluted_market_cap = fully_diluted_market_cap;
            }

            public String getLast_updated() {
                return last_updated;
            }

            public void setLast_updated(String last_updated) {
                this.last_updated = last_updated;
            }
        }
    }
}
