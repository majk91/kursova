package com.mike.kursova_oop_db.data.engines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mike.kursova_oop_db.data.db.DBHelper;
import com.mike.kursova_oop_db.data.models.Basket;
import com.mike.kursova_oop_db.data.models.BasketItems;
import com.mike.kursova_oop_db.data.models.Category;
import com.mike.kursova_oop_db.data.models.FormMsg;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.data.models.Order;
import com.mike.kursova_oop_db.data.models.OrderItem;
import com.mike.kursova_oop_db.data.models.Review;
import com.mike.kursova_oop_db.data.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

public class DatabaseEngine {

    private DatabaseEngine() {}

    public static class Holder {
        public static DatabaseEngine INSTANCE = new DatabaseEngine();
    }

    public static DatabaseEngine getInstance() {
        return Holder.INSTANCE;
    }

    //USER
    public long storeUser(final Context ctx, final User user){
        long result = -1;
        if (ctx == null || user == null ) {
            return result;
        }
        final SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(User.NAME, user.getName());
        contentValues.put(User.EMAIL, user.getEmail());
        contentValues.put(User.PASS, user.getPass());
        contentValues.put(User.PHOTO, user.getPhoto());
        contentValues.put(User.ROLE, user.getRole().name());

        if (db != null) {
            if(user.getId()>0){
                result =  db.update(User.TABLE_NAME, contentValues,User._ID+"=?",
                        new String[]{valueOf(user.getId())});
            }else{
                result = db.insertWithOnConflict(User.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.close();
        }
        return result;
    }

    public boolean deleteUser(final Context context, final int id) {
        final SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        boolean r = db.delete(User.TABLE_NAME, User._ID + "=?", new String[]{valueOf(id)}) > 0;
        db.close();
        return r;
    }

    public User getUserById(final Context ctx, final long id){
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'", User.TABLE_NAME, User._ID, id);
        List<User> users = getUsersByFilter(ctx, sqlQuery);
        if(users.size()>0)
            return  users.get(0);
        else
            return new User();
    }

    public User getUserByEmail(final Context ctx, final String email){
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'", User.TABLE_NAME, User.EMAIL, email);
        List<User> users = getUsersByFilter(ctx, sqlQuery);
        if(users.size()>0)
            return  users.get(0);
        else
            return new User();
    }

    public List<User> getUsersByFilter(final Context context, final String query){
        final List<User> data = new ArrayList<>();
        final SQLiteDatabase db = new DBHelper(context).getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex(User._ID)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(User.EMAIL)));
                user.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                user.setPass(cursor.getString(cursor.getColumnIndex(User.PASS)));
                user.setPhoto(cursor.getString(cursor.getColumnIndex(User.PHOTO)));
                String role = cursor.getString(cursor.getColumnIndex(User.ROLE));
                if(!role.isEmpty())
                    user.setRole(User.Status.valueOf(role));

                data.add(user);
            }
        }
        cursor.close();
        db.close();
        return data;

    }

    //BASKET
    public long storeBasketItem(final Context ctx, final BasketItems basket, final long userId){
        long result = -1;
        if (ctx == null || basket == null ) {
            return result;
        }
        Basket bs = getBasketByUserIdGoodId(ctx, userId, basket.getGoodId());
        if(bs.getId()>0){
            basket.setId(bs.getId());
        }
        final SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Basket.GOOD_ID, basket.getGoodId());
        contentValues.put(Basket.GOODS_COUNT, basket.getCount());
        contentValues.put(Basket.USER_ID, userId);


        if (db != null) {
            if(basket.getId()<=0){
                result = db.insertWithOnConflict(Basket.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
            }else{
                result =  db.update(Basket.TABLE_NAME, contentValues,Basket._ID+"=?",
                        new String[]{valueOf(basket.getId())});
            }
            db.close();
        }
        return result;
    }

    public boolean deleteBasket(final Context context, final long userId) {
        final SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        boolean r = db.delete(Basket.TABLE_NAME, Basket.USER_ID + "=?", new String[]{valueOf(userId)}) > 0;
        db.close();
        return r;
    }

    public boolean deleteBasketItem(final Context context, final BasketItems basket) {
        final SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        boolean r = db.delete(Basket.TABLE_NAME, Basket._ID + "=?", new String[]{valueOf(basket.getId())}) > 0;
        db.close();
        return r;
    }

    public Basket getBasketByUserId(final Context ctx, final Long userId){
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'", Basket.TABLE_NAME, Basket.USER_ID, userId);
        return getBasketByFilter(ctx, sqlQuery);
    }

    public Basket getBasketByUserIdGoodId(final Context ctx, final Long userId, final Long goodId){
        String sqlQuery = String.format(
                "SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' ",
                Basket.TABLE_NAME,
                Basket.USER_ID,
                userId,
                Basket.GOOD_ID,
                goodId
        );
        return getBasketByFilter(ctx, sqlQuery);
    }

    public Basket getBasketByFilter(final Context ctx, final String query){
        final HashMap<Long, BasketItems> data = new HashMap<>();
        Basket basket = new Basket();
        int count = 0;
        final SQLiteDatabase db = new DBHelper(ctx).getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            try{
                while (cursor.moveToNext()) {
                    BasketItems item = new BasketItems();
                    item.setId(cursor.getLong(cursor.getColumnIndex(Basket._ID)));
                    item.setGoodId(cursor.getLong(cursor.getColumnIndex(Basket.GOOD_ID)));
                    item.setCount(cursor.getInt(cursor.getColumnIndex(Basket.GOODS_COUNT)));
                    Good g = getGoodById(ctx, item.getGoodId());
                    item.setGood(g);

                    data.put(item.getGoodId(), item);

                    count+=item.getCount();
                    basket.setUserId(cursor.getLong(cursor.getColumnIndex(Basket.USER_ID)));
                    basket.setId(cursor.getLong(cursor.getColumnIndex(Basket._ID)));
                }
                basket.setCount(count);
                basket.setGoods(data);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                cursor.close();
            }
        }
        db.close();
        return basket;
    }

    //CATEGORY
    public long storeCategory(final Context ctx, final Category category){
        long result = -1;
        if (ctx == null || category == null ) {
            return result;
        }
        final SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Category.NAME, category.getName());
        contentValues.put(Category.PHOTO, category.getPhotoURI());
        contentValues.put(Category.ACTIVE, category.getActive()? 1 : 0);

        if (db != null) {
            if(category.getId()<=0){
                result = db.insertWithOnConflict(Category.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
            }else{
                result =  db.update(Category.TABLE_NAME, contentValues,Category._ID+"=?",
                        new String[]{valueOf(category.getId())});
            }
            db.close();
        }
        return result;
    }

    public boolean deleteCategory(final Context context, final Long id) {
        final SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        boolean r = db.delete(Category.TABLE_NAME, Category._ID + "=?", new String[]{valueOf(id)}) > 0;
        db.close();
        return r;
    }

    public List<Category> getCategories(final Context ctx){
        String sqlQuery = String.format("SELECT * FROM %s", Category.TABLE_NAME);
        return  getCategoriesByFilter(ctx, sqlQuery);
    }

    public List<Category> getCategoriesByFilter(final Context context, final String query){
        final List<Category> data = new ArrayList<>();
        final SQLiteDatabase db = new DBHelper(context).getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Category item = new Category();
                item.setId(cursor.getLong(cursor.getColumnIndex(Category._ID)));
                item.setName(cursor.getString(cursor.getColumnIndex(Category.NAME)));
                item.setPhotoURI(cursor.getString(cursor.getColumnIndex(Category.PHOTO)));
                int active = cursor.getInt(cursor.getColumnIndex(Category.ACTIVE));
                item.setActive(active > 0);

                data.add(item);
            }
        }
        cursor.close();
        db.close();
        return data;
    }

    //FORM
    public long storeFormMsg(final Context ctx, final FormMsg msg){
        long result = -1;
        if (ctx == null || msg == null ) {
            return result;
        }
        final SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(FormMsg.NAME, msg.getName());
        contentValues.put(FormMsg.EMAIL, msg.getEmail());
        contentValues.put(FormMsg.MESSAGE, msg.getMsg());
        contentValues.put(FormMsg.PHONE, msg.getPhone());

        Calendar rightNow = Calendar.getInstance();
        contentValues.put(FormMsg.TIME, rightNow.getTimeInMillis());

        if (db != null) {
            if(msg.getId()<=0){
                result = db.insertWithOnConflict(FormMsg.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
            }else{
                result =  db.update(FormMsg.TABLE_NAME, contentValues,FormMsg._ID+"=?",
                        new String[]{valueOf(msg.getId())});
            }
            db.close();
        }
        return result;
    }

    public boolean deleteFormMsg(final Context context, final int id) {
        final SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        boolean r = db.delete(FormMsg.TABLE_NAME, FormMsg._ID + "=?", new String[]{valueOf(id)}) > 0;
        db.close();
        return r;
    }

    public List<FormMsg> getForms(final Context ctx){
        String sqlQuery = String.format("SELECT * FROM %s", FormMsg.TABLE_NAME);
        return getFormMsgByFilter(ctx, sqlQuery);
    }

    public List<FormMsg> getFormMsgByFilter(final Context context, final String query){
        final List<FormMsg> data = new ArrayList<>();
        final SQLiteDatabase db = new DBHelper(context).getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                FormMsg item = new FormMsg();
                item.setId(cursor.getLong(cursor.getColumnIndex(FormMsg._ID)));
                item.setEmail(cursor.getString(cursor.getColumnIndex(FormMsg.EMAIL)));
                item.setName(cursor.getString(cursor.getColumnIndex(FormMsg.NAME)));
                item.setPhone(cursor.getString(cursor.getColumnIndex(FormMsg.PHONE)));
                item.setMsg(cursor.getString(cursor.getColumnIndex(FormMsg.MESSAGE)));
                item.setTime(cursor.getLong(cursor.getColumnIndex(FormMsg.TIME)));
                data.add(item);
            }
        }
        cursor.close();
        db.close();
        return data;
    }

    //GOODS
    public long storeGood(final Context ctx, final Good good){
        long result = -1;
        if (ctx == null || good == null ) {
            return result;
        }
        final SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Good.NAME, good.getName());
        contentValues.put(Good.VENDOR_CODE, good.getVendorCode());
        contentValues.put(Good.PRICE, good.getPrice());
        contentValues.put(Good.PHOTO, good.getPhotoURI());
        contentValues.put(Good.DESCRIPTION, good.getDescription());
        contentValues.put(Good.COUNT, good.getCount());
        contentValues.put(Good.CATEGORY_ID, good.getCatId());
        contentValues.put(Good.ACTIVE, good.getActive()?"1":"0");

        if (db != null) {
            if(good.getId()<=0){
                result = db.insertWithOnConflict(Good.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
            }else{
                result =  db.update(Good.TABLE_NAME, contentValues,Good._ID+"=?",
                        new String[]{valueOf(good.getId())});
            }
            db.close();
        }
        return result;
    }

    public boolean deleteGood(final Context context, final long id) {
        final SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        boolean r = db.delete(Good.TABLE_NAME, Good._ID + "=?", new String[]{valueOf(id)}) > 0;
        db.close();
        return r;
    }

    public Good getGoodById(final Context ctx, final long id){
        String sqlQuery = "SELECT *  FROM " + Good.TABLE_NAME + " WHERE " + Good._ID + " = "+id;
        List<Good> goods = getGoodsByFilter(ctx, sqlQuery);
        if(goods.size()>0)
            return  goods.get(0);
        else
            return new Good();
    }

    public List<Good> getGoods(final Context ctx){
        String sqlQuery = "SELECT g.*, c." + Category.NAME + " as "+Good.CATEGORY_NAME
                + " FROM " + Good.TABLE_NAME + " g "
                + " JOIN " + Category.TABLE_NAME + " c "
                + " ON g."+ Good.CATEGORY_ID + " = c." + Category._ID;
        return getGoodsByFilter(ctx, sqlQuery);
    }

    public List<Good> getGoodsByCategory(final Context ctx, final long categoryId){
        String sqlQuery = "SELECT g.*, c." + Category.NAME + " as "+Good.CATEGORY_NAME
                + " FROM " + Good.TABLE_NAME + " g "
                + " JOIN " + Category.TABLE_NAME + " c "
                + " ON g."+ Good.CATEGORY_ID + " = c." + Category._ID
                + " WHERE "+ Good.CATEGORY_ID +" = '"+categoryId+"'";

        return getGoodsByFilter(ctx, sqlQuery);
    }

    public List<Good> getGoodsByCategoryIdSort(final Context ctx, final long categoryId, final String sortBy, final String order){
        String sqlQuery = "SELECT g.*, c." + Category.NAME + " as "+Good.CATEGORY_NAME
                + " FROM " + Good.TABLE_NAME + " g "
                + " JOIN " + Category.TABLE_NAME + " c "
                + " ON g."+ Good.CATEGORY_ID + " = c." + Category._ID
                + " WHERE "+ Good.CATEGORY_ID +" = '"+categoryId+"'"
                + " ORDER BY "+sortBy+" "+order;
        return getGoodsByFilter(ctx, sqlQuery);
    }

    public List<Good> getGoodsBySort(final Context ctx, final String sortBy, final String order){
        String sqlQuery = "SELECT g.*, c." + Category.NAME + " as "+Good.CATEGORY_NAME
                + " FROM " + Good.TABLE_NAME + " g "
                + " JOIN " + Category.TABLE_NAME + " c "
                + " ON g."+ Good.CATEGORY_ID + " = c." + Category._ID
                + " ORDER BY "+sortBy+" "+order;
        return getGoodsByFilter(ctx, sqlQuery);
    }

    public List<Good> getGoodsByFilter(final Context ctx, final String query){
        final List<Good> data = new ArrayList<>();
        final SQLiteDatabase db = new DBHelper(ctx).getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    Good item = new Good();
                    item.setId(cursor.getLong(cursor.getColumnIndex(Good._ID)));
                    item.setCatId(cursor.getLong(cursor.getColumnIndex(Good.CATEGORY_ID)));
                    item.setCount(cursor.getInt(cursor.getColumnIndex(Good.COUNT)));
                    item.setVendorCode(cursor.getString(cursor.getColumnIndex(Good.VENDOR_CODE)));
                    item.setPrice(cursor.getDouble(cursor.getColumnIndex(Good.PRICE)));
                    item.setPhotoURI(cursor.getString(cursor.getColumnIndex(Good.PHOTO)));
                    item.setName(cursor.getString(cursor.getColumnIndex(Good.NAME)));
                    item.setDescription(cursor.getString(cursor.getColumnIndex(Good.DESCRIPTION)));
                    item.setReviews(getReviewsByGoodId(ctx, item.getId()));
                    int index = cursor.getColumnIndex(Good.CATEGORY_NAME);
                    if(index>0){
                        String catName = cursor.getString(index);
                        item.setCatName(catName);
                    }
                    int active = cursor.getInt(cursor.getColumnIndex(Good.ACTIVE));
                    item.setActive(active > 0);

                    data.add(item);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                cursor.close();
            }
        }
        db.close();
        return data;
    }

    //ORDER ITEM
    public long storeOrderItem(final Context ctx, final OrderItem orderItem){
        long result = -1;
        if (ctx == null || orderItem == null ) {
            return result;
        }
        final SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(OrderItem.GOOD_ID, orderItem.getGoodId());
        contentValues.put(OrderItem.GOODS_COUNT, orderItem.getCount());
        contentValues.put(OrderItem.ORDER_ID, orderItem.getOrderId());

        if (db != null) {
            if(orderItem.getId()<=0){
                result = db.insertWithOnConflict(OrderItem.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
            }else{
                result =  db.update(OrderItem.TABLE_NAME, contentValues,OrderItem._ID+"=?",
                        new String[]{valueOf(orderItem.getId())});
            }
            db.close();
        }
        return result;
    }

    public boolean deleteOrderItem(final Context context, final int id) {
        final SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        boolean r = db.delete(OrderItem.TABLE_NAME, OrderItem._ID + "=?", new String[]{valueOf(id)}) > 0;
        db.close();
        return r;
    }

    public List<OrderItem> getOrdersItemsByParentOrderId(final Context ctx, final long orderId){
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'", OrderItem.TABLE_NAME, OrderItem.ORDER_ID, orderId);
        return getOrdersItemByFilter(ctx, sqlQuery);
    }

    public List<OrderItem> getOrdersItemByFilter(final Context ctx, final String query){
        final List<OrderItem> data = new ArrayList<>();
        final SQLiteDatabase db = new DBHelper(ctx).getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(cursor.getLong(cursor.getColumnIndex(OrderItem._ID)));
                orderItem.setCount(cursor.getInt(cursor.getColumnIndex(OrderItem.GOODS_COUNT)));
                orderItem.setOrderId(cursor.getLong(cursor.getColumnIndex(OrderItem.ORDER_ID)));
                orderItem.setGoodId(cursor.getLong(cursor.getColumnIndex(OrderItem.GOOD_ID)));
                orderItem.setGood(getGoodById(ctx, orderItem.getGoodId()));

                data.add(orderItem);
            }
        }
        cursor.close();
        db.close();
        return data;
    }

    //ORDER
    public long storeOrderAll(final Context ctx, final Order order){
        long orderId = storeOrder(ctx, order);

        if(orderId<0)
            return -1;

        for(OrderItem item: order.getItems()){
            item.setOrderId(orderId);
            storeOrderItem(ctx, item);
        }

        return orderId;
    }

    public long storeOrder(final Context ctx, final Order order){
        long result = -1;
        if (ctx == null || order == null ) {
            return result;
        }
        final SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Order.GOODS_COUNT, order.getCount());
        contentValues.put(Order.TOTAL_PRISE, order.getPrise());
        contentValues.put(Order.USER_ID, order.getUserId());
        contentValues.put(Order.STATUS, order.getStatus().toString());

        Calendar rightNow = Calendar.getInstance();
        contentValues.put(Order.TIME, rightNow.getTimeInMillis());

        if (db != null) {
            if(order.getId()<=0){
                result = db.insertWithOnConflict(Order.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_IGNORE);
            }else{
                result =  db.update(Order.TABLE_NAME, contentValues,Order._ID+"=?",
                        new String[]{valueOf(order.getId())});
            }
            db.close();
        }
        return result;
    }

    public boolean deleteOrder(final Context context, final int id) {
        final SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        boolean r = db.delete(Order.TABLE_NAME, Order._ID + "=?", new String[]{valueOf(id)}) > 0;
        db.close();
        return r;
    }

    public long updateStatus(final Context ctx, final long orderId, final Order.Status status){
        /*String query = "UPDATE "+Order.TABLE_NAME + " SET "+Order.STATUS+" = '"+status.name()
                +"' WHERE "+Order._ID+ " = "+orderId;*/
        long result = -1;
        if (ctx == null || status == null )
            return result;

        final SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Order.STATUS, status.toString());

        if (db != null) {
            result =  db.update(Order.TABLE_NAME, contentValues,Order._ID+"=?", new String[]{valueOf(orderId)});
            db.close();
        }
        return result;
    }

    public List<Order> getOrders(final Context ctx){
        String sqlQuery = "SELECT "
                + "orderT.*, "
                + "userT." + User.NAME + " as "+Order.USER_NAME
                +", userT." + User.EMAIL + " as "+Order.USER_EMAIL
                + " FROM " + Order.TABLE_NAME + " orderT "
                + " JOIN " + User.TABLE_NAME + " userT "
                + " ON orderT."+ Order.USER_ID + " = userT." + User._ID;
        return getOrdersByFilter(ctx, sqlQuery);
    }

    public List<Order> getOrdersByUserId(final Context ctx, final long userId){
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'", Order.TABLE_NAME, Order.USER_ID, userId);
        return getOrdersByFilter(ctx, sqlQuery);
    }

    public List<Order> getOrdersByFilter(final Context ctx, final String query){
        final List<Order> data = new ArrayList<>();
        final SQLiteDatabase db = new DBHelper(ctx).getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Order item = new Order();
                item.setId(cursor.getLong(cursor.getColumnIndex(Order._ID)));
                item.setUserId(cursor.getLong(cursor.getColumnIndex(Order.USER_ID)));
                item.setPrise(cursor.getDouble(cursor.getColumnIndex(Order.TOTAL_PRISE)));
                item.setCount(cursor.getInt(cursor.getColumnIndex(Order.GOODS_COUNT)));
                item.setTime(cursor.getLong(cursor.getColumnIndex(Order.TIME)));
                String status = cursor.getString(cursor.getColumnIndex(Order.STATUS));
                if(!status.isEmpty())
                    item.setStatus(Order.Status.valueOf(status));

                int index = cursor.getColumnIndex(Order.USER_NAME);
                if(index>0) item.setUserName(cursor.getString(index));
                index = cursor.getColumnIndex(Order.USER_EMAIL);
                if(index>0) item.setUserEmail(cursor.getString(index));

                item.setItems(getOrdersItemsByParentOrderId(ctx, item.getId()));

                data.add(item);
            }
        }
        cursor.close();
        db.close();
        return data;
    }

    //REVIEW
    public long storeReview(final Context ctx, final Review review){
        long result = -1;
        if (ctx == null || review == null ) {
            return result;
        }
        final SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Review.USER_ID, review.getUserId());
        contentValues.put(Review.GOOD_ID, review.getGoodId());
        contentValues.put(Review.MESSAGE, review.getMsg());
        contentValues.put(Review.RATING, review.getRating());

        Calendar rightNow = Calendar.getInstance();
        contentValues.put(Review.TIME, rightNow.getTimeInMillis());

        if (db != null) {
            if(review.getId()<=0){
                result = db.insertWithOnConflict(Review.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }else{
                result =  db.update(Review.TABLE_NAME, contentValues,Review._ID+"=?", new String[]{valueOf(review.getId())});
            }
            db.close();
        }
        return result;
    }

    public boolean deleteReview(final Context context, final int id) {
        final SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        boolean r = db.delete(Review.TABLE_NAME, Review._ID + "=?", new String[]{valueOf(id)}) > 0;
        db.close();
        return r;
    }

    public List<Review> getReviewsByGoodId(final Context ctx, final long goodId){
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'", Review.TABLE_NAME, Review.GOOD_ID, goodId);
        List<Review> list = getReviewsByFilter(ctx, sqlQuery);
        return list;
    }

    public List<Review> getReviewsByFilter(final Context context, final String query){
        final List<Review> data = new ArrayList<>();
        final SQLiteDatabase db = new DBHelper(context).getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Review item = new Review();
                item.setId(cursor.getLong(cursor.getColumnIndex(Review._ID)));
                item.setMsg(cursor.getString(cursor.getColumnIndex(Review.MESSAGE)));
                item.setRating(cursor.getInt(cursor.getColumnIndex(Review.RATING)));
                item.setUserId(cursor.getLong(cursor.getColumnIndex(Review.USER_ID)));
                item.setGoodId(cursor.getLong(cursor.getColumnIndex(Review.GOOD_ID)));

                data.add(item);
            }
        }
        cursor.close();
        db.close();
        return data;

    }
}
