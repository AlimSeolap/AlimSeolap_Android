package com.whysly.alimseolap1.views.Activity;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.whysly.alimseolap1.interfaces.MyService;
import com.whysly.alimseolap1.models.daos.NotificationDao;
import com.whysly.alimseolap1.models.databases.NotificationDatabase;
import com.whysly.alimseolap1.models.entities.NotificationEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends AndroidViewModel {
    LiveData<NotificationEntity> entity = new MutableLiveData<>();
    LiveData<List<NotificationEntity>> entities_all; // = NotificationDatabase.getNotificationDatabase().notificationDao().loadAllNotificationLiveData();
    LiveData<List<NotificationEntity>> entities_positive;
    LiveData<List<NotificationEntity>> entities_negative;
    LiveData<List<NotificationEntity>> entities_default;


    private NotificationDatabase ndb;
    private NotificationDao notiDao;

    long id;

    public MainViewModel(@NonNull Application application) {
        super(application);

        ndb = NotificationDatabase.getNotificationDatabase(application);
        notiDao = ndb.notificationDao();
        entities_all = notiDao.loadAllNotificationLiveData();
        entities_positive = notiDao.loadPositiveNotificationLiveData();
        entities_negative = notiDao.loadNegativeNotificationLiveData();
        entities_default = notiDao.loadDefaultNotificationLiveData();

    }

    public NotificationDao getNotificationDao() {
        return notiDao;
    }

    //모든 노티피케이션을 가져옵니다.
    public LiveData<List<NotificationEntity>> getAllNotification() {
        return entities_all;
    }

    //처음 노티 크롤링할때 유저평가 0인 디폴트 노티피케이션을 가져옵니다.
    public LiveData<List<NotificationEntity>> getDefaultNotifications() {
        return entities_default;
    }

    //관심 노티피케이션을 가져옵니다.
    public LiveData<List<NotificationEntity>> getPositiveNotification() {
        return entities_positive;
    }

    //무관심 노티피케이션을 가져옵니다.
    public LiveData<List<NotificationEntity>> getNegativeNotification() {
        return entities_negative;
    }

    public LiveData<NotificationEntity> LoadNotification(long id) {
        //entity = notiDao.loadNotification(id);
        return entity;
    }



    // TODO 카테고리별로 다 위에 처럼 만들어 놓기.



    public void insert(NotificationEntity entity) {
        notiDao.insertNotification(entity);
    }

    public void  updateRealEvaluation(long id, long this_user_real_evaluation, String token) {

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.30.1.18:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("현우", "Retrofit 빌드 성공");
        Boolean evaluate;

//            NotificationDatabase db = NotificationDatabase.getNotificationDatabase(getContext());
//            user_id = db.notificationDao().loadNotification(noti_id).user_id;
//            notititle = db.notificationDao().loadNotification(noti_id).title;
        if (this_user_real_evaluation == 1) {
            evaluate = true;
        } else {
            evaluate = false;
        }




        MyService service = retrofit.create(MyService.class);
        //json 객체 생성하여 값을 넣어줌
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("evaluation", evaluate);


        Call<JsonObject> call = service.patchEvaluation(token, String.valueOf(notiDao.loadNotification(id).server_id), jsonObject );
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("알림데이터 전송성공");
                Log.d("현우", response.toString());
                Log.d("현우", retrofit.toString());


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("알림데이터 전송실패");
                Log.d("현우", t.toString());


            }
        });
        notiDao.updateRealEvaluation(id, this_user_real_evaluation);
    }



}
