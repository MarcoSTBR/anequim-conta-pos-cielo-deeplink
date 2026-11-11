package br.com.st.anequimconta_stand_dl1.pospag.payCielo.orders;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cielo.ordermanager.sdk.orders.OrderManagerConnector;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cielo.orders.domain.CancellationRequest;
import cielo.orders.domain.CheckoutRequest;
import cielo.orders.domain.Order;
import cielo.orders.domain.ResultOrders;
import cielo.orders.domain.product.PrimaryProduct;
import cielo.sdk.order.cancellation.CancellationListener;
import cielo.sdk.order.payment.PaymentCode;
import cielo.sdk.order.payment.PaymentListener;

public class OrderManagerControllerJava {
    private static final String TAG = "OrderManagerController";

    private static volatile OrderManagerControllerJava instance;

    private final OrderManagerConnectorJava orderManagerConnector;
    private final ExecutorService executorService;
    private List<PaymentCode> enabledProducts;

    private OrderManagerControllerJava(Context context) {
        this.orderManagerConnector = new OrderManagerConnectorJava(context);
        this.executorService = Executors.newCachedThreadPool();
        this.enabledProducts = Collections.emptyList();
    }

    public static OrderManagerControllerJava getInstance(Context context) {
        if (instance == null) {
            synchronized (OrderManagerControllerJava.class) {
                if (instance == null) {
                    instance = new OrderManagerControllerJava(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    // Equivalente à função 'suspend' getOrderManager
    private cielo.sdk.order.OrderManager getOrderManager() throws Exception {
        return orderManagerConnector.getOrderManager();
    }

    public boolean isServiceBound() {
        return orderManagerConnector.getServiceBoundState();
    }

    public void refreshOrdersFromServer() {
        executorService.execute(() -> {
            try {
                getOrderManager().refreshOrdersFromServer();
            } catch (Exception e) {
                Log.e(TAG, "Erro ao atualizar pedidos do servidor", e);
            }
        });
    }

    @Nullable
    public ResultOrders getOrders(int pageSize, int page) {
        try {
            return getOrderManager().retrieveOrders(pageSize, page);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar pedidos", e);
            return null;
        }
    }

    @Nullable
    public Order createDraftOrder(String orderReference) {
        try {
            return getOrderManager().createDraftOrder(orderReference);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar pedido rascunho", e);
            return null;
        }
    }

    public void checkout(CheckoutRequest checkoutRequest, PaymentListener paymentListener) {
        executorService.execute(() -> {
            try {
                getOrderManager().checkoutOrder(checkoutRequest, paymentListener);
            } catch (Exception e) {
                Log.e(TAG, "Erro no checkout", e);
            }
        });
    }

    public void cancelOrder(CancellationRequest cancellationRequest, CancellationListener cancellationListener) {
        executorService.execute(() -> {
            try {
                getOrderManager().cancelOrder(cancellationRequest, cancellationListener);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao cancelar pedido", e);
            }
        });
    }

    public void updateOrder(Order order) {
        executorService.execute(() -> {
            try {
                getOrderManager().updateOrder(order);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao atualizar pedido", e);
            }
        });
    }

    public void placeOrder(Order order) {
        executorService.execute(() -> {
            try {
                getOrderManager().placeOrder(order);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao efetivar pedido", e);
            }
        });
    }

    public void fetchEnabledProducts() {
        executorService.execute(() -> {
            try {
                List<PaymentCode> products = getOrderManager().retrieveEnabledProducts();
                if (products != null) {
                    enabledProducts = products;
                } else {
                    enabledProducts = Collections.emptyList();
                }
                Log.d(TAG, "SDK return: " + enabledProducts);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao buscar produtos habilitados", e);
            }
        });
    }

    @NonNull
    public List<PaymentCode> getEnabledProducts() {
        return enabledProducts;
    }

    @Nullable
    public List<PrimaryProduct> getPrimaryProducts(Context context) {
        try {
            return getOrderManager().retrievePaymentType(context);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar produtos primários", e);
            return null;
        }
    }

    @Nullable
    public ResultOrders retrieveOrders(int pageSize, int page) {
        try {
            return getOrderManager().retrieveOrders(pageSize, page);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao recuperar pedidos", e);
            return null;
        }
    }

    @Nullable
    public Order findOrderById(String orderId) {
        try {
            return getOrderManager().findOrderById(orderId);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao encontrar pedido por ID", e);
            return null;
        }
    }
}
