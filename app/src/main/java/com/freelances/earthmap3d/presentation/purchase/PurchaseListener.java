package com.freelances.earthmap3d.presentation.purchase;

public interface PurchaseListener {
    void onProductPurchased(String productId, String transactionDetails);
    void displayErrorMessage(String errorMsg );
    void onUserCancelBilling();
    void onUserPurchaseConsumable();
}
