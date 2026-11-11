package br.com.st.anequimconta_stand_dl1.pospag.payCielo.orders;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

import br.com.st.anequimconta_stand_dl1.BuildConfig;
import cielo.orders.domain.Credentials;
import cielo.sdk.order.OrderManager;
import cielo.sdk.order.ServiceBindListener;

public class OrderManagerConnectorJava implements ServiceBindListener {
    private static final String TAG = "OrderManagerConnector";

    private final Context context;
    private final OrderManager orderManagerInstance;
    private boolean serviceBoundState = false;
    private final ReentrantLock bindLock = new ReentrantLock();

    // Future para aguardar o bind de forma assíncrona/síncrona se necessário
    private CompletableFuture<OrderManager> bindFuture;

    public OrderManagerConnectorJava(Context context) {
        this.context = context.getApplicationContext();
        this.orderManagerInstance = new OrderManager(
                new Credentials(BuildConfig.CREDENTIALS_CLIENT_ID, BuildConfig.CREDENTIALS_ACCESS_TOKEN),
                this.context
        );
        this.bindFuture = new CompletableFuture<>();
    }

    public boolean getServiceBoundState() {
        return serviceBoundState;
    }

    private synchronized void updateBoundStatus(boolean bound) {
        Log.d(TAG, "Emitting bound: " + bound);
        serviceBoundState = bound;
        if (bound) {
            if (bindFuture.isDone()) {
                bindFuture = new CompletableFuture<>();
            }
            bindFuture.complete(orderManagerInstance);
        }
        Log.d(TAG, "Emit done");
    }

    public OrderManager getOrderManager() throws Exception {
        bindLock.lock();
        try {
            if (!serviceBoundState) {
                Log.d(TAG, "Calling SDK Bind");
                orderManagerInstance.bind(context, this);
            }
            // Aguarda o serviço estar vinculado (equivalente ao first() do Flow)
            return bindFuture.get();
        } finally {
            bindLock.unlock();
        }
    }

    @Override
    public void onServiceBound() {
        Log.d(TAG, "Service bound");
        updateBoundStatus(true);
    }

    @Override
    public void onServiceUnbound() {
        Log.d(TAG, "Service unbound");
        updateBoundStatus(false);
    }

    @Override
    public void onServiceBoundError(Throwable throwable) {
        Log.d(TAG, "Service onServiceBoundError: " + throwable);
        onServiceUnbound();
        if (!bindFuture.isDone()) {
            bindFuture.completeExceptionally(throwable);
        }
    }
}
