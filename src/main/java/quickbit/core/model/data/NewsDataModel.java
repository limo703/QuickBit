package quickbit.core.model.data;

import java.util.List;

public class NewsDataModel {
    private String items;
    private String sentiment_score_definition;
    private String relevance_score_definition;
    private List<FeedItem> feed;

    public String getItems() {
        return items;
    }

    public NewsDataModel setItems(String items) {
        this.items = items;
        return this;
    }

    public String getSentiment_score_definition() {
        return sentiment_score_definition;
    }

    public NewsDataModel setSentiment_score_definition(String sentiment_score_definition) {
        this.sentiment_score_definition = sentiment_score_definition;
        return this;
    }

    public String getRelevance_score_definition() {
        return relevance_score_definition;
    }

    public NewsDataModel setRelevance_score_definition(String relevance_score_definition) {
        this.relevance_score_definition = relevance_score_definition;
        return this;
    }

    public List<FeedItem> getFeed() {
        return feed;
    }

    public NewsDataModel setFeed(List<FeedItem> feed) {
        this.feed = feed;
        return this;
    }

    public static class FeedItem {
        private String title;
        private String url;
        private String time_published;
        private List<String> authors;
        private String summary;
        private String banner_image;
        private String source;
        private String category_within_source;
        private String source_domain;
        private List<Topic> topics;
        private double overall_sentiment_score;
        private String overall_sentiment_label;
        private List<TickerSentiment> ticker_sentiment;

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

        public String getTime_published() {
            return time_published;
        }

        public FeedItem setTime_published(String time_published) {
            this.time_published = time_published;
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

        public String getBanner_image() {
            return banner_image;
        }

        public FeedItem setBanner_image(String banner_image) {
            this.banner_image = banner_image;
            return this;
        }

        public String getSource() {
            return source;
        }

        public FeedItem setSource(String source) {
            this.source = source;
            return this;
        }

        public String getCategory_within_source() {
            return category_within_source;
        }

        public FeedItem setCategory_within_source(String category_within_source) {
            this.category_within_source = category_within_source;
            return this;
        }

        public String getSource_domain() {
            return source_domain;
        }

        public FeedItem setSource_domain(String source_domain) {
            this.source_domain = source_domain;
            return this;
        }

        public List<Topic> getTopics() {
            return topics;
        }

        public FeedItem setTopics(List<Topic> topics) {
            this.topics = topics;
            return this;
        }

        public double getOverall_sentiment_score() {
            return overall_sentiment_score;
        }

        public FeedItem setOverall_sentiment_score(double overall_sentiment_score) {
            this.overall_sentiment_score = overall_sentiment_score;
            return this;
        }

        public String getOverall_sentiment_label() {
            return overall_sentiment_label;
        }

        public FeedItem setOverall_sentiment_label(String overall_sentiment_label) {
            this.overall_sentiment_label = overall_sentiment_label;
            return this;
        }

        public List<TickerSentiment> getTicker_sentiment() {
            return ticker_sentiment;
        }

        public FeedItem setTicker_sentiment(List<TickerSentiment> ticker_sentiment) {
            this.ticker_sentiment = ticker_sentiment;
            return this;
        }
    }

    public static class Topic {
        private String topic;
        private String relevance_score;

        public String getTopic() {
            return topic;
        }

        public Topic setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public String getRelevance_score() {
            return relevance_score;
        }

        public Topic setRelevance_score(String relevance_score) {
            this.relevance_score = relevance_score;
            return this;
        }
    }

    public static class TickerSentiment {
        private String ticker;
        private String relevance_score;
        private String ticker_sentiment_score;
        private String ticker_sentiment_label;

        public String getTicker() {
            return ticker;
        }

        public TickerSentiment setTicker(String ticker) {
            this.ticker = ticker;
            return this;
        }

        public String getRelevance_score() {
            return relevance_score;
        }

        public TickerSentiment setRelevance_score(String relevance_score) {
            this.relevance_score = relevance_score;
            return this;
        }

        public String getTicker_sentiment_score() {
            return ticker_sentiment_score;
        }

        public TickerSentiment setTicker_sentiment_score(String ticker_sentiment_score) {
            this.ticker_sentiment_score = ticker_sentiment_score;
            return this;
        }

        public String getTicker_sentiment_label() {
            return ticker_sentiment_label;
        }

        public TickerSentiment setTicker_sentiment_label(String ticker_sentiment_label) {
            this.ticker_sentiment_label = ticker_sentiment_label;
            return this;
        }
    }
}