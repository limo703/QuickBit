package quickbit.core.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsDataModel {
    private String items;

    @JsonProperty("sentiment_score_definition")
    private String sentimentScoreDefinition;

    @JsonProperty("relevance_score_definition")
    private String relevanceScoreDefinition;

    private List<FeedItem> feed;

    public String getItems() {
        return items;
    }

    public NewsDataModel setItems(String items) {
        this.items = items;
        return this;
    }

    public String getSentimentScoreDefinition() {
        return sentimentScoreDefinition;
    }

    public NewsDataModel setSentimentScoreDefinition(String sentimentScoreDefinition) {
        this.sentimentScoreDefinition = sentimentScoreDefinition;
        return this;
    }

    public String getRelevanceScoreDefinition() {
        return relevanceScoreDefinition;
    }

    public NewsDataModel setRelevanceScoreDefinition(String relevanceScoreDefinition) {
        this.relevanceScoreDefinition = relevanceScoreDefinition;
        return this;
    }

    public List<FeedItem> getFeed() {
        return feed;
    }

    public NewsDataModel setFeed(List<FeedItem> feed) {
        this.feed = feed;
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeedItem {
        private String title;
        private String url;
        private String timePublished;

        private List<String> authors;
        private String summary;
        private String bannerImage;
        private String source;
        private String categoryWithinSource;
        private String sourceDomain;

        private List<Topic> topics;

        @JsonProperty("overall_sentiment_score")
        private double overallSentimentScore;

        @JsonProperty("overall_sentiment_label")
        private String overallSentimentLabel;

        private List<TickerSentiment> tickerSentiment;

        public String getTitle() {
            return title;
        }

        public FeedItem setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public FeedItem setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getTimePublished() {
            return timePublished;
        }

        public FeedItem setTimePublished(String timePublished) {
            this.timePublished = timePublished;
            return this;
        }

        public List<String> getAuthors() {
            return authors;
        }

        public FeedItem setAuthors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public String getSummary() {
            return summary;
        }

        public FeedItem setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public String getBannerImage() {
            return bannerImage;
        }

        public FeedItem setBannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
            return this;
        }

        public String getSource() {
            return source;
        }

        public FeedItem setSource(String source) {
            this.source = source;
            return this;
        }

        public String getCategoryWithinSource() {
            return categoryWithinSource;
        }

        public FeedItem setCategoryWithinSource(String categoryWithinSource) {
            this.categoryWithinSource = categoryWithinSource;
            return this;
        }

        public String getSourceDomain() {
            return sourceDomain;
        }

        public FeedItem setSourceDomain(String sourceDomain) {
            this.sourceDomain = sourceDomain;
            return this;
        }

        public List<Topic> getTopics() {
            return topics;
        }

        public FeedItem setTopics(List<Topic> topics) {
            this.topics = topics;
            return this;
        }

        public double getOverallSentimentScore() {
            return overallSentimentScore;
        }

        public FeedItem setOverallSentimentScore(double overallSentimentScore) {
            this.overallSentimentScore = overallSentimentScore;
            return this;
        }

        public String getOverallSentimentLabel() {
            return overallSentimentLabel;
        }

        public FeedItem setOverallSentimentLabel(String overallSentimentLabel) {
            this.overallSentimentLabel = overallSentimentLabel;
            return this;
        }

        public List<TickerSentiment> getTickerSentiment() {
            return tickerSentiment;
        }

        public FeedItem setTickerSentiment(List<TickerSentiment> tickerSentiment) {
            this.tickerSentiment = tickerSentiment;
            return this;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Topic {
        private String topic;
        private double relevanceScore;

        public String getTopic() {
            return topic;
        }

        public Topic setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public double getRelevanceScore() {
            return relevanceScore;
        }

        public Topic setRelevanceScore(double relevanceScore) {
            this.relevanceScore = relevanceScore;
            return this;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TickerSentiment {
        private String ticker;
        private double relevanceScore;

        @JsonProperty("ticker_sentiment_score")
        private double tickerSentimentScore;

        @JsonProperty("ticker_sentiment_label")
        private String tickerSentimentLabel;

        public String getTicker() {
            return ticker;
        }

        public TickerSentiment setTicker(String ticker) {
            this.ticker = ticker;
            return this;
        }

        public double getRelevanceScore() {
            return relevanceScore;
        }

        public TickerSentiment setRelevanceScore(double relevanceScore) {
            this.relevanceScore = relevanceScore;
            return this;
        }

        public double getTickerSentimentScore() {
            return tickerSentimentScore;
        }

        public TickerSentiment setTickerSentimentScore(double tickerSentimentScore) {
            this.tickerSentimentScore = tickerSentimentScore;
            return this;
        }

        public String getTickerSentimentLabel() {
            return tickerSentimentLabel;
        }

        public TickerSentiment setTickerSentimentLabel(String tickerSentimentLabel) {
            this.tickerSentimentLabel = tickerSentimentLabel;
            return this;
        }
    }

}
