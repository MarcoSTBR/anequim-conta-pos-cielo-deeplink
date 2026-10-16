package br.com.st.anequimconta_stand_dl1.pospag.payCielo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.cielo.ordermanager.sdk.sample.services.DeepLinkService;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class UtilisJava {
    public static final String STOP_SERVICE = "STOP_SERVICE";
    private static final Gson GSON = new Gson();
    private static final Random RANDOM = new Random();

    private UtilisJava() {
        // Construtor privado para evitar a instanciação da classe utilitária
    }

    public static String getAmount(long value) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR"))
                .format(value / 100.0f);
    }

    public static String getBase64(String json) {
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static void startForegroundServiceAndLaunchDeepLink(Context context, String deepLink) {
        Intent serviceIntent = new Intent(context, DeepLinkService.class);
        context.startService(serviceIntent);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLink));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callToStopService(Context context) {
        Intent stopServiceIntent = new Intent(context, DeepLinkService.class);
        stopServiceIntent.setAction(STOP_SERVICE);
        context.startService(stopServiceIntent);
    }

    public static void addText(ViewGroup viewGroup, String text) {
        TextView textView = new TextView(viewGroup.getContext());
        textView.setText(text);
        viewGroup.addView(textView);
    }

    public static String getQueryParameter(Intent intent, String name) {
        Uri data = intent.getData();
        return data != null ? data.getQueryParameter(name) : null;
    }

    public static String decodeBase64(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static <T> T deserializeJson(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    public static <T> void deserializeQueryParameter(Intent intent, String name, Class<T> type, BiConsumer<String, T> block) {
        String param = getQueryParameter(intent, name);
        if (param != null) {
            String decoded = decodeBase64(param);
            T deserialized = deserializeJson(decoded, type);
            block.accept(decoded, deserialized);
        }
    }

    public static void deserializeQueryParameter(Intent intent, String name, Consumer<String> block) {
        String param = getQueryParameter(intent, name);
        if (param != null) {
            String decoded = decodeBase64(param);
            block.accept(decoded);
        }
    }

    public static Uri saveImage(Context context, Bitmap bitmap) {
        File storageDir = context.getExternalFilesDir(null);
        if (storageDir == null) {
            storageDir = Environment.getExternalStorageDirectory();
        }
        return createImageFile(storageDir, bitmap, Uri::fromFile);
    }

    public static Uri saveImageWithProvider(Context context, Bitmap bitmap) {
        File storageDir = context.getFilesDir();
        return createImageFile(storageDir, bitmap, file -> createFileProviderUri(context, file));
    }

    private static Uri createImageFile(File storageDir, Bitmap bitmap, UriConverter toUri) {
        try {
            File imageDirectory = new File(storageDir, "saved_images");
            if (!imageDirectory.exists()) {
                imageDirectory.mkdirs();
            }
            int randomSuffix = RANDOM.nextInt(9000) + 1000;
            String fileName = "Image-" + randomSuffix + ".jpg";
            File fileImage = new File(imageDirectory, fileName);
            if (fileImage.exists()) {
                fileImage.delete();
            }

            try (FileOutputStream outputStream = new FileOutputStream(fileImage)) {
                boolean compressionSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                if (!compressionSuccess) {
                    throw new IOException("Failed to compress bitmap");
                }
            }
            return toUri.convert(fileImage);
        } catch (Exception e) {
            e.printStackTrace();
            return Uri.EMPTY;
        }
    }

    private static Uri createFileProviderUri(Context context, File file) {
        try {
            String authority = context.getPackageName() + ".fileprovider";
            Uri uri = FileProvider.getUriForFile(context, authority, file);
            grantUriPermission(context, uri);
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
            return Uri.EMPTY;
        }
    }

    private static void grantUriPermission(Context context, Uri targetUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("lio://print"));
        Set<String> packageNames = new HashSet<>();

        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)) {
            if (resolveInfo.activityInfo != null) {
                packageNames.add(resolveInfo.activityInfo.packageName);
            }
        }

        for (String packageName : packageNames) {
            try {
                context.grantUriPermission(
                        packageName,
                        targetUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Interfaces auxiliares equivalentes aos blocos/lambdas do Kotlin
    public interface BiConsumer<T, U> {
        void accept(T t, U u);
    }

    public interface Consumer<T> {
        void accept(T t);
    }

    private interface UriConverter {
        Uri convert(File file);
    }
}
