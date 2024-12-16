# Green Grocer

## Overview

The Green Grocer App is a comprehensive mobile application designed to facilitate online grocery shopping. It allows users to browse, select, and purchase a wide variety of grocery items from the comfort of their homes. The app is built using Java and leverages Firebase for backend services, including database management and authentication.

## Features

### **User Features**
- **User Registration and Login:** Users can create accounts and log in with their email and password.
- **Browse Items:** Explore various categories such as Fruits, Vegetables, Dairy, Proteins, Grains.
- **Search and Filter:** Search for specific items and filter results based on categories.
- **Add to Cart:** Add items to the shopping cart and adjust quantities using the `ChangeNumberItemsListener`.
- **Order Placement:** Place orders for selected items in the cart.
- **Profile Management:** View and update profile information.
- **Forgot Password:** Reset password if forgotten.
- **View Global Grocery Price Trends:** Users can explore worldwide grocery market insights through [this link](https://api.myjson.online/v1/records/f39c83c3-8382-41e2-89bb-a0ad48973222), where they can see:
  - Total revenue for renowned shops by category.
  - Categories with high and low demand.
  - Estimated price growth for the business year.

### **Admin Features**
- **Admin Dashboard:** Access an administrative panel to manage app content.
- **Add Items:** Add new grocery items with product details such as name, price, description, and image.
- **Edit and Delete Items:** Update or delete existing product listings.
- **Order Management:** View and update the status of customer orders.
- **Order Tracking:** View order status, from pending to dispatched and received.
- **Revenue Tracking:** Monitor total revenue and completed orders.

## Technical Details

### **Technologies Used**
- **Languages:** Java, XML
- **Database:** Firebase Realtime Database
- **Authentication:** Firebase Authentication
- **JSON Parsing:** Integrated with RapidAPI using MyJson for dynamic grocery price trend updates
- **Image Loading:** Glide (for dynamic image fetching)
- **RecyclerView:** For displaying product lists
- **Architecture Components:** ViewModel, LiveData

### **Project Structure**
- **Activities:** Includes `MainActivity`, `RegisterActivity`, `CartActivity`, `AddItemActivity`, and more.
- **Adapters:** `BestDealsAdapter`, `CartAdapter`, `ListItemAdapter` (with corrected use of `ItemsModel` instead of `ItemsDomain`).
- **Domain:** Data models such as `ItemsModel`, `OrderModel`, `PendingOrderModel`.
- **Helpers:** `ChangeNumberItemsListener`, `ManagementCart`, `TinyDB` (local data storage).
- **Repository:** Manages Firebase database interactions.
- **ViewModel:** Manages UI-related data efficiently.

### **Installation and Setup**
1. Clone the repository:
   ```sh
   git clone https://github.com/Afifa637/Grocery-Shop.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle files.
4. Configure Firebase:
   - Add `google-services.json` to the `app` directory.
   - Enable Firebase Authentication and Realtime Database in the Firebase console.
5. Build and run the app on an emulator or physical device.

## Additional Features
- **Dynamic Image Loading:** Images are fetched dynamically from the `images` folder in the GitHub repository.
- **User Role Management:** Different interfaces and functionalities for Admins and Customers.
- **Error Tracking:** Integrated tools like Firebase Crashlytics (recommended for app monitoring).

## Contributing
Contributions are welcome! Fork the repository and create a pull request with proposed changes.

## Contact
For inquiries or support, contact [Afifa637](https://github.com/Afifa637).
