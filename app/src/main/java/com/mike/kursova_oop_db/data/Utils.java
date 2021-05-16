package com.mike.kursova_oop_db.data;

import android.content.Context;
import android.net.Uri;

import com.mike.kursova_oop_db.data.engines.DatabaseEngine;
import com.mike.kursova_oop_db.data.models.Category;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.data.models.User;
import com.mike.kursova_oop_db.data.repositories.ShopInteractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Utils {

    private ShopInteractor dataShopInteractor;

    public static class Holder {
        public static Utils INSTANCE = new Utils();
    }

    public static Utils getInstance() {
        return Utils.Holder.INSTANCE;
    }

    public Utils() {
        dataShopInteractor = new ShopInteractor();
    }

    public void createAdmin(Context ctx){
        Disposable d= dataShopInteractor.getUserByEmail(ctx, "admin@admin")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(user -> {
                if(user.getId()<=0){
                    dataShopInteractor.storeUser(ctx, new User("Admin", "admin@admin", "11111", "", User.Status.ADMIN)).subscribe();
                }
        }, error -> {});
    }

    public void createDemoShop(Context ctx){

        Disposable d= downloadDemoShop(ctx).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bool -> {}, error -> {});

    }

    private Single<Boolean> downloadDemoShop(Context ctx){
        return Single.fromCallable(() -> {

            List<Category> list = DatabaseEngine.getInstance().getCategories(ctx);

            if(list.size()>0)
                return false;

            Uri uriCategoryPizza = Uri.parse(downloadImage(ctx, "http://pf-ganichi.uz.ua/wp-content/uploads/2021/04/pizza.png", "p"));
            Uri uriCategorySalad = Uri.parse(downloadImage(ctx, "http://pf-ganichi.uz.ua/wp-content/uploads/2021/04/salad_ico.png", "s"));
            Uri uriCategorySushi = Uri.parse(downloadImage(ctx, "http://pf-ganichi.uz.ua/wp-content/uploads/2021/04/sushi_ico.png", "su"));
            Uri uriCategoryDrink = Uri.parse(downloadImage(ctx, "http://pf-ganichi.uz.ua/wp-content/uploads/2021/04/soft_drink.png", "d"));

            //Создать 4 категории
            Category catPizza = new Category("Piza", uriCategoryPizza.toString(), true);
            long cat1 = DatabaseEngine.getInstance().storeCategory(ctx, catPizza);

            Category catSalad = new Category("Salad", uriCategorySalad.toString(), true);
            long cat2 = DatabaseEngine.getInstance().storeCategory(ctx, catSalad);

            Category catSushi = new Category("Sushi", uriCategorySushi.toString(), true);
            long cat3 = DatabaseEngine.getInstance().storeCategory(ctx, catSushi);

            Category catDrink = new Category("Drink", uriCategoryDrink.toString(), true);
            long cat4 = DatabaseEngine.getInstance().storeCategory(ctx, catDrink);

            Uri uriPizza = Uri.parse(downloadImage(ctx, "http://pf-ganichi.uz.ua/wp-content/uploads/2021/04/picca.jpeg", "pizza"));
            Uri uriSalad = Uri.parse(downloadImage(ctx, "http://pf-ganichi.uz.ua/wp-content/uploads/2021/04/salad.jpeg", "salad"));
            Uri uriSushi = Uri.parse(downloadImage(ctx, "http://pf-ganichi.uz.ua/wp-content/uploads/2021/04/sushi.jpeg", "sushi"));
            Uri uriDrink = Uri.parse(downloadImage(ctx, "http://pf-ganichi.uz.ua/wp-content/uploads/2021/04/drink.jpeg", "drink"));

            String lorem = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

            //Создать 10 товаров
            Good good1 = new Good("PIZZA NINJA", "10001", uriPizza.toString(), 55, lorem, cat1, 10, true);
            Good good2 = new Good("PIZZA Big", "10002", uriPizza.toString(), 67, lorem, cat1, 10, true);
            Good good3 = new Good("PIZZA Carbonara", "10003", uriPizza.toString(), 90, lorem, cat1, 10, true);
            Good good4 = new Good("Caesar", "20001", uriSalad.toString(), 55, lorem, cat2, 10, true);
            Good good5 = new Good("Caesar2", "20002", uriSalad.toString(), 155, lorem, cat2, 10, true);
            Good good6 = new Good("Dragon", "30001", uriSushi.toString(), 1111, lorem, cat3, 10, true);
            Good good7 = new Good("Fila", "30002", uriSushi.toString(), 735, lorem, cat3, 10, true);
            Good good8 = new Good("Borjomi", "40001", uriDrink.toString(), 55, lorem, cat4, 10, true);
            Good good9 = new Good("Chivas", "40002", uriDrink.toString(), 55, lorem, cat4, 10, true);
            Good good10 = new Good("Lemon Juice", "40003", uriDrink.toString(), 55, lorem, cat4, 10, true);

            DatabaseEngine.getInstance().storeGood(ctx, good1);
            DatabaseEngine.getInstance().storeGood(ctx, good2);
            DatabaseEngine.getInstance().storeGood(ctx, good3);
            DatabaseEngine.getInstance().storeGood(ctx, good4);
            DatabaseEngine.getInstance().storeGood(ctx, good5);
            DatabaseEngine.getInstance().storeGood(ctx, good6);
            DatabaseEngine.getInstance().storeGood(ctx, good7);
            DatabaseEngine.getInstance().storeGood(ctx, good8);
            DatabaseEngine.getInstance().storeGood(ctx, good9);
            DatabaseEngine.getInstance().storeGood(ctx, good10);

            return true;
        });
    }

    private String downloadImage(Context ctx, String url, String random){

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        String extension = url.substring(url.lastIndexOf(".") + 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
        String date = dateFormat.format(new Date());

        String fileDir = String.format("%s/direct/", ctx.getFilesDir());
        String fileName = String.format("%s%s_%s.%s", fileDir, date, random, extension);

        File mydir = new File(fileDir);
        if (!mydir.exists()) mydir.mkdirs();

        try {
            URL Url = new URL(url);
            connection = (HttpURLConnection) Url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
            }
            input = connection.getInputStream();


            output = new FileOutputStream(fileName);

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {}

            if (connection != null)
                connection.disconnect();
        }

        return Uri.parse(fileName).toString();
    }
}
