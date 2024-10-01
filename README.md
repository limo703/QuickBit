# QuickBit Dashboard

## Overview

This project is a **QuickBit Dashboard** designed to display real-time cryptocurrency data, wallet balances, fiat currencies, user information, and recent news updates. The dashboard provides users with key information about crypto markets and their financial positions.

## Features

- **Real-Time Crypto Charts**: Displays live price trends for multiple cryptocurrencies, such as BTC, ETH, DOGE, TON, PEPE, and others.
- **User Wallets**: Shows the balances of the user's crypto wallets.
- **Fiat Currencies**: Displays the user's fiat currency balances (e.g., USD, RUB, EUR).
- **News Section**: Provides the latest news related to the crypto market.
- **User Profile Information**: Displays user details like avatar, name, and other information.
- **Responsive Design**: The interface adapts to different screen sizes, making it easy to use on desktops and mobile devices.

## Tech Stack

- **Backend**: Java, Spring Boot, Redis, PostgreSQL
- **Frontend**: Thymeleaf, JavaScript, Chart.js, HTML/CSS
- **Database**: PostgreSQL for data storage
- **Caching**: Redis for fast access to frequently updated data like news
- **Authentication**: Spring Security for user authentication and authorization
- **Currency Data**: Real-time and historical cryptocurrency price data sourced from external APIs

## Installation

### Prerequisites

- Java 11 or higher
- Maven or Gradle
- PostgreSQL
- Redis

### Steps

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-repo/crypto-dashboard.git
    cd crypto-dashboard
    ```

2. **Configure the application**:
   Update the `application.properties` or `application.yml` file with your database and Redis connection details.

3. **Build the project**:
    ```bash
    mvn clean install
    ```

4. **Run the project**:
    ```bash
    mvn spring-boot:run
    ```

5. **Access the application**:
   Open your browser and navigate to `http://localhost:8080`.

## Usage

Once the application is running:

1. Log in using your credentials.
2. View live cryptocurrency prices and their changes on the homepage.
3. Check your crypto wallet and fiat currency balances.
4. Stay updated with the latest crypto market news.
5. View and edit your profile information as needed.

## Future Improvements

- **Transaction System**: Add the ability to make crypto transactions directly from the dashboard.
- **Customizable Charts**: Allow users to select time intervals or customize the chart displays.
- **Notification System**: Implement notifications for price changes or important news.

## Contributing

Contributions are welcome! You can open issues, create pull requests, or leave feedback to help improve the project.

## License

This project is licensed under the MIT License.
