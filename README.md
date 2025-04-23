# Shoply Backend

A Spring Boot-based backend service for a price comparison and shopping list management application.

## Overview

Shoply is a mobile application that helps users compare prices of products across different stores, track price changes over time, and manage shopping lists. This repository contains the backend API that powers the application.

## Features

- **Authentication**
  - Local email/password authentication
  - Google OAuth integration
  - JWT-based session management

- **Barcode Scanning and Product Data**
  - Integration with OpenFoodFacts API for product information
  - Price tracking for products across different stores
  - Product comparison with AI assistance

- **Shopping Lists**
  - Create and manage shopping lists
  - Add products to shopping lists with preferred stores
  - Track shopping list items

- **Store Management**
  - Google Places API integration for finding stores
  - Geospatial queries for finding nearby stores
  - Store data management

- **Price Comparison**
  - Track product prices across different stores
  - Historical price data

## Technology Stack

- **Framework**: Spring Boot
- **Database**: MongoDB (with geospatial indexing)
- **Authentication**: JWT (JSON Web Tokens)
- **External APIs**:
  - OpenFoodFacts API for product data
  - Google Places API for store locations
  - OpenAI API for product comparisons
- **Storage**: AWS S3 for image storage

## Architecture

The application follows a standard Spring Boot architecture:

- **Controllers**: Handle HTTP requests and define API endpoints
- **Services**: Implement business logic
- **Repositories**: Interface with the MongoDB database
- **Models**: Define data structures
- **DTOs**: For data transfer between client and server
- **Utilities**: Helper classes for external API integration and other functionality

## API Endpoints

### Authentication

- `POST /api/auth/register`: Register a new user
- `POST /api/auth/login`: Login with email and password
- Google OAuth endpoints

### Products

- `GET /api/products`: Get all products
- `GET /api/products/{id}`: Get product by ID
- `GET /api/products/barcode/{barcode}`: Get product by barcode
- `GET /api/products/compare/{barcode}`: Get or create product by barcode
- `POST /api/products/compare/ai`: Compare two products using AI
- `POST /api/products`: Create a new product
- `DELETE /api/products/{id}`: Delete a product

### Barcode Scans

- `GET /api/barcode-scans`: Get all barcode scans
- `GET /api/barcode-scans/{id}`: Get scan by ID
- `GET /api/barcode-scans/user/{userId}`: Get scans by user ID
- `POST /api/barcode-scans`: Create a new scan
- `POST /api/barcode-scans/track`: Track a scan and update price data
- `DELETE /api/barcode-scans/{id}`: Delete a scan

### Shopping Lists

- `GET /api/shopping-lists/user/{userId}`: Get user's shopping lists
- `POST /api/shopping-lists`: Create a new shopping list
- `POST /api/shopping-lists/{shoppingListId}/items`: Add item to shopping list
- `GET /api/shopping-lists/{shoppingListId}/items`: Get items for a shopping list
- `DELETE /api/shopping-lists/{id}`: Delete a shopping list

### Stores

- `GET /api/stores`: Get stores near a location (within 200m)
- `POST /api/stores`: Get stores near a location (within 25km)
- `GET /api/stores/{id}`: Get store by ID
- `DELETE /api/stores/{id}`: Delete a store

### Price Comparisons

- `GET /api/price-comparisons`: Get all price comparisons
- `GET /api/price-comparisons/{id}`: Get comparison by ID
- `GET /api/price-comparisons/product/{productId}`: Get comparisons by product ID
- `GET /api/price-comparisons/store/{storeId}`: Get comparisons by store ID
- `POST /api/price-comparisons`: Create a new price comparison
- `DELETE /api/price-comparisons/{id}`: Delete a price comparison

### Users

- `GET /api/users`: Get all users
- `GET /api/users/{id}`: Get user by ID
- `POST /api/users`: Create a new user
- `DELETE /api/users/{id}`: Delete a user

## Setup and Configuration

### Prerequisites

- Java 17+
- MongoDB
- AWS Account (for S3 integration)
- Google API credentials (for Places API and OAuth)
- OpenAI API key

### Environment Variables

The application requires the following environment variables:

- `MONGODB_URI`: MongoDB connection string
- `PORT`: Server port
- `JWT_SECRET`: Secret key for JWT token generation (Base64 encoded)
- `AWS_ACCESS_KEY_ID`: AWS access key
- `AWS_SECRET_ACCESS_KEY`: AWS secret key
- `AWS_REGION`: AWS region
- `GOOGLE_PLACES_API_KEY`: Google Places API key
- `GOOGLE_OAUTH_CLIENT_ID`: Google OAuth client ID
- `GOOGLE_CLIENT_SECRET`: Google OAuth client secret
- `GOOGLE_OAUTH_REDIRECT_URI`: Google OAuth redirect URI
- `CHAT_GPT_API_KEY`: OpenAI API key

### Running the Application

1. Clone the repository
2. Set the required environment variables
3. Run the application:
