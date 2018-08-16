package cl.activaresearch.android_app.Dooit.servercommunication;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.activaresearch.android_app.Dooit.models.AccountBean;
import cl.activaresearch.android_app.Dooit.models.BankBean;
import cl.activaresearch.android_app.Dooit.models.CategoryBean;
import cl.activaresearch.android_app.Dooit.models.PaymentBean;
import cl.activaresearch.android_app.Dooit.models.PaymentDetails;
import cl.activaresearch.android_app.Dooit.models.RegionBean;
import cl.activaresearch.android_app.Dooit.models.SurveyBean;
import cl.activaresearch.android_app.Dooit.models.TaskBean;
import cl.activaresearch.android_app.Dooit.models.TaskQuestionType;
import cl.activaresearch.android_app.Dooit.models.UserBean;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 21 Jun,2018
 */
public class ApiHelper {
    private static ApiHelper helper;
    private static List<RegionBean> regionBeans = new ArrayList<>();
    private static List<CategoryBean> categories = new ArrayList<>();
    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    public ApiHelper() {
    }

    public static ApiHelper getInstance() {
        if (helper == null) {
            helper = new ApiHelper();
        }
        return helper;
    }

    public void userLogin(HashMap<String, String> body, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.login(body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject1 = new JSONObject(body);
                        String message = null;
                        if (jsonObject1.has("message")) {
                            message = jsonObject1.getString("message");
                        }
                        boolean success = false;
                        if (jsonObject1.has("success")) {
                            success = jsonObject1.getBoolean("success");
                        }
                        if (jsonObject1.has("error")) {
                            String error = jsonObject1.getString("error");
                            callback.onFailure(error);
                        }
                        if (success) {
                            String token = jsonObject1.getString("token");
                            callback.onSuccess(token);
                        } else {
                            callback.onFailure(message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void userFacebookLogin(HashMap<String, String> body, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.fbLogin(body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject1 = new JSONObject(body);
                        boolean success = jsonObject1.getBoolean("success");
                        if (success) {
                            String id = jsonObject1.getString("identifier");
                            callback.onSuccess(id);
                        } else {
                            callback.onFailure("error");
                        }


                       /* boolean success = jsonObject1.getBoolean("success");
                        if (success) {
                            String strEmail = edtEmail.getText().toString().trim();
                            String strPassword = edtPassword.getText().toString().trim();
                            HashMap<String, String> body1 = new HashMap<>();
                            body1.put("email", strEmail);
                            body1.put("password", strPassword);
                            loginProcess(body1, b);
                        } else {
                            showToast(message);
                            dismissProgress();
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("", "");
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        callback.onFailure(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t.getMessage());
                t.printStackTrace();
                Log.d("", "");
            }
        });

    }

    public void userForgotPassword(HashMap<String, String> body, final ApiCallback callback) {

    }

    public void userSignUp(final HashMap<String, String> bodys, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.register(bodys);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject1 = new JSONObject(body);
                        String message = jsonObject1.getString("message");
                        boolean success = jsonObject1.getBoolean("success");
                        if (success) {
                            userLogin(bodys, new ApiCallback.Listener() {
                                @Override
                                public void onSuccess(String result) {
                                    callback.onSuccess(result);
                                }

                                @Override
                                public void onFailure(String error) {
                                    callback.onFailure(error);
                                }
                            });
                        } else {
                            callback.onFailure(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("", "");
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t.getMessage());
                t.printStackTrace();
                Log.d("", "");
            }
        });

    }

    public void updateUserProfileDetails(HashMap<String, String> body, String token, final ApiCallback.Listener callback) {
        Call<ResponseBody> responseBodyCall = apiService.update(token, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject1 = new JSONObject(body);
                        boolean success = jsonObject1.getBoolean("success");
                        if (success) {
                            callback.onSuccess(null);
                        } else {
                            callback.onFailure(null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void getUserProfileDetails(String token, final ApiCallback.UserListener callback) {
        Call<UserBean> responseBodyCall = apiService.details(token);
        responseBodyCall.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                if (response.body() != null) {
                    try {
                        callback.onSuccess(response.body());
                    } catch (Exception e) {
                        callback.onFailure(e.getMessage());
                        e.printStackTrace();
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void getRegionAndCity(String token, final ApiCallback.RegionsListener callback) {
        if (regionBeans.size() > 0) {
            callback.onSuccess(regionBeans);
            return;
        }
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<RegionBean>> responseBodyCall = apiService.city(token);
        responseBodyCall.enqueue(new Callback<List<RegionBean>>() {
            @Override
            public void onResponse(Call<List<RegionBean>> call, Response<List<RegionBean>> response) {
                if (response.body() != null) {
                    try {
                        regionBeans.clear();
                        regionBeans.addAll(response.body());
                        callback.onSuccess(regionBeans);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<List<RegionBean>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void getAllCategory(String token, final ApiCallback.CategoryListener callback) {
        if (categories.size() > 0) {
            callback.onSuccess(categories);
            return;
        }
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<CategoryBean>> responseBodyCall = apiService.category(token);
        responseBodyCall.enqueue(new Callback<List<CategoryBean>>() {
            @Override
            public void onResponse(Call<List<CategoryBean>> call, Response<List<CategoryBean>> response) {
                if (response.body() != null) {
                    try {
                        categories.clear();
                        categories.addAll(response.body());
                        callback.onSuccess(categories);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<List<CategoryBean>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void getAllTask(String token, String lat, String lng, String distance, String categories, final ApiCallback.TasksListener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<TaskBean>> responseBodyCall = apiService.geo(token, lat, lng, distance, categories);
        responseBodyCall.enqueue(new Callback<List<TaskBean>>() {
            @Override
            public void onResponse(Call<List<TaskBean>> call, Response<List<TaskBean>> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        List<TaskBean> list = response.body();
                        callback.onSuccess(list);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<List<TaskBean>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void getTaskDetails(String token, String taskId, final ApiCallback.TaskListener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TaskBean> responseBodyCall = apiService.taskDetails(token, taskId);
        responseBodyCall.enqueue(new Callback<TaskBean>() {
            @Override
            public void onResponse(Call<TaskBean> call, Response<TaskBean> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        callback.onSuccess(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<TaskBean> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }

    public void getCurrentTask(String token, final ApiCallback.TasksListener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<TaskBean>> responseBodyCall = apiService.currentTask(token);
        responseBodyCall.enqueue(new Callback<List<TaskBean>>() {
            @Override
            public void onResponse(Call<List<TaskBean>> call, Response<List<TaskBean>> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        callback.onSuccess(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<List<TaskBean>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });

    }


    public void getPreviousTask(String token, final ApiCallback.TasksListener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<TaskBean>> responseBodyCall = apiService.previousTask(token);
        responseBodyCall.enqueue(new Callback<List<TaskBean>>() {
            @Override
            public void onResponse(Call<List<TaskBean>> call, Response<List<TaskBean>> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        callback.onSuccess(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<List<TaskBean>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void getTaskSurvey(String token, String id, final ApiCallback.SurveyListener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.questions(token, id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONArray jsonArray = new JSONArray(body);
                        List<SurveyBean> surveyBeans = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            SurveyBean surveyBean = new SurveyBean();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String question = jsonObject.getString("question");
                            int type = jsonObject.getInt("type");
                            String cat = jsonObject.getString("cat");
                            String id = jsonObject.getString("id");
                            String studyId = jsonObject.getString("studyId");
                            String questionnaireId = jsonObject.getString("questionnaireId");
                            String taskId = jsonObject.getString("taskId");
                            surveyBean.setType(TaskQuestionType.valueOf(type));
                            surveyBean.setQuestion(question);
                            surveyBean.setCat(cat);
                            surveyBean.setId(id);
                            surveyBean.setStudyId(studyId);
                            surveyBean.setQuestionnaireId(questionnaireId);
                            surveyBean.setTaskId(taskId);
                            //check alternatives there or not
                            if (jsonObject.has("alternatives")) {
                                JSONArray alternatives = jsonObject.getJSONArray("alternatives");
                                List<SurveyBean.AlternativesBean> alternativesBeans = new ArrayList<>();
                                for (int j = 0; j < alternatives.length(); j++) {
                                    SurveyBean.AlternativesBean bean = new SurveyBean.AlternativesBean();
                                    JSONObject jsonObject1 = alternatives.getJSONObject(j);
                                    String text = jsonObject1.getString("text");
                                    int id1 = jsonObject1.getInt("id");
                                    String cod = jsonObject1.getString("cod");
                                    bean.setAnswered(false);
                                    bean.setCod(cod);
                                    bean.setId(id1);
                                    bean.setText(text);
                                    alternativesBeans.add(bean);
                                }
                                surveyBean.setAlternatives(alternativesBeans);
                            }

                            //check answer is there or not
                            if (jsonObject.has("answer")) {
                                surveyBean.setAnswered(true);
                                JSONObject answer = jsonObject.getJSONObject("answer");
                                String date = answer.getString("date");
                                SurveyBean.AnswerBean answerBean = new SurveyBean.AnswerBean();
                                answerBean.setDate(date);
                                switch (type) {
                                    case 1:
                                        String data = answer.getString("data");
                                        answerBean.setDataBean(data);
                                        break;
                                    case 2:
                                        String data1 = answer.getString("data");
                                        answerBean.setDataBean(data1);
                                        break;
                                    case 3:
                                        JSONObject data3 = answer.getJSONObject("data");
                                        List<Integer> dataBean = new ArrayList<>();
                                        int alternative = data3.getInt("alternative");
                                        dataBean.add(alternative);
                                        answerBean.setDataBean(dataBean);
                                        break;
                                    case 4:
                                        JSONArray data4 = answer.getJSONArray("data");
                                        List<Integer> dataBean1 = new ArrayList<>();
                                        for (int j = 0; j < data4.length(); j++) {
                                            JSONObject object = data4.getJSONObject(j);
                                            int alternative1 = object.getInt("alternative");
                                            dataBean1.add(alternative1);
                                        }
                                        answerBean.setDataBean(dataBean1);
                                        break;
                                    case 5:
                                        String data5 = answer.getString("data");
                                        answerBean.setDataBean(data5);
                                        break;
                                    case 6:
                                        String data6 = answer.getString("data");
                                        answerBean.setDataBean(data6);
                                        break;
                                    case 7:
                                        String data7 = answer.getString("data");
                                        answerBean.setDataBean(data7);
                                        break;
                                }
                                surveyBean.setAnswer(answerBean);
                            } else {
                                surveyBean.setAnswered(false);
                            }
                            surveyBeans.add(surveyBean);
                        }
                        callback.onSuccess(surveyBeans);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void getTaskShopperSurvey(String token, final ApiCallback.SurveyListener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.shopperQuestions(token);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONArray jsonArray = new JSONArray(body);
                        List<SurveyBean> surveyBeans = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            SurveyBean surveyBean = new SurveyBean();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String question = jsonObject.getString("question");
                            String id = jsonObject.getString("id");
                            surveyBean.setId(id);
                            surveyBean.setType(TaskQuestionType.valueOf(3));
                            surveyBean.setQuestion(question);                            //check alternatives there or not
                            if (jsonObject.has("alternatives")) {
                                JSONArray alternatives = jsonObject.getJSONArray("alternatives");
                                List<SurveyBean.AlternativesBean> alternativesBeans = new ArrayList<>();
                                for (int j = 0; j < alternatives.length(); j++) {
                                    SurveyBean.AlternativesBean bean = new SurveyBean.AlternativesBean();
                                    JSONObject jsonObject1 = alternatives.getJSONObject(j);
                                    String text = jsonObject1.getString("text");
                                    int id1 = jsonObject1.getInt("id");
                                    boolean checked = jsonObject1.getBoolean("checked");
                                    if (checked) {
                                        surveyBean.setAnswered(checked);
                                    }
                                    bean.setId(id1);
                                    bean.setText(text);
                                    bean.setAnswered(checked);
                                    alternativesBeans.add(bean);
                                }
                                surveyBean.setAlternatives(alternativesBeans);
                            }
                            surveyBeans.add(surveyBean);
                        }
                        callback.onSuccess(surveyBeans);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void ansShopperSurveyQuestion(String token, String qid, JsonObject body, final ApiCallback.Listener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.shopperAnswer(token, qid, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            String message = jsonObject.getString("message");
                            callback.onSuccess(message);
                        } else {
                            String error = jsonObject.getString("error");
                            callback.onFailure(error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");

            }
        });
    }

    public void ansSurveyQuestion(String token, String id, String qid, JsonObject body, final ApiCallback.Listener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.answer(token, id, qid, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            String message = jsonObject.getString("message");
                            callback.onSuccess(message);
                        } else {
                            String error = jsonObject.getString("error");
                            callback.onFailure(error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");

            }
        });
    }

    public void getBalance(String token, final ApiCallback.Listener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.currentBalance(token);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        callback.onSuccess(jsonObject.getString("balance"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void claimForTask(String token, String id, final ApiCallback.Listener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.taskClaim(token, id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        callback.onSuccess("Claim successfully");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void completeTask(String token, String id, final ApiCallback.Listener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.complete(token, id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            callback.onSuccess(null);
                        } else {
                            callback.onFailure(null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }


    public void getPaymentHistory(String token, final ApiCallback.PaymentListener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.payHistory(token);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        List<PaymentBean> paymentBeans = new ArrayList<>();
                        JSONArray pending = jsonObject.getJSONArray("pending");
                        for (int i = 0; i < pending.length(); i++) {
                            JSONObject jsonObject1 = pending.getJSONObject(i);
                            PaymentBean paymentBean = new Gson().fromJson(jsonObject1.toString(), PaymentBean.class);
                            paymentBeans.add(paymentBean);
                        }
                        JSONArray paid = jsonObject.getJSONArray("paid");
                        JSONArray rejected = jsonObject.getJSONArray("rejected");
                        callback.onSuccess(paymentBeans);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void paymentDetails(String token, String id, final ApiCallback.PaymentDetailsListener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PaymentDetails> responseBodyCall = apiService.payDetails(token, id);
        responseBodyCall.enqueue(new Callback<PaymentDetails>() {
            @Override
            public void onResponse(Call<PaymentDetails> call, Response<PaymentDetails> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        callback.onSuccess(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentDetails> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void registerDevice(String token, JsonObject body, final ApiCallback.Listener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.register(token, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        boolean success = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");
                        if (success) {
                            callback.onSuccess(message);
                        } else {
                            callback.onFailure(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void toggleNotifications(String token, JsonObject body, final ApiCallback.Listener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.toggle(token, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            String message = jsonObject.getString("message");
                            callback.onSuccess(message);
                        } else {
                            callback.onFailure("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void withdrawFund(String token, final ApiCallback.Listener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.cashout(token);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            callback.onSuccess(null);
                        } else {
                            String error = jsonObject.getString("error");
                            callback.onFailure(error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    private static List<BankBean> banks = new ArrayList<>();

    public void getBanks(String token, final ApiCallback.BanksListener callback) {
        if (banks.size() > 0) {
            callback.onSuccess(banks);
            return;
        }
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<BankBean>> responseBodyCall = apiService.banks(token);
        responseBodyCall.enqueue(new Callback<List<BankBean>>() {
            @Override
            public void onResponse(Call<List<BankBean>> call, Response<List<BankBean>> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        callback.onSuccess(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<List<BankBean>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }


    public void getBankAccount(String token, final ApiCallback.AccountListener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AccountBean> responseBodyCall = apiService.account(token);
        responseBodyCall.enqueue(new Callback<AccountBean>() {
            @Override
            public void onResponse(Call<AccountBean> call, Response<AccountBean> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        callback.onSuccess(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<AccountBean> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }

    public void setBankAccount(String token, JsonObject jsonObject, final ApiCallback.Listener callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.addAccount(token, jsonObject);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Log.d("", response.body() + "");
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            callback.onSuccess("");
                        } else {
                            String message = jsonObject.getString("message");
                            callback.onFailure(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                        Log.d("", "");
                    }
                } else {
                    try {
                        String body = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String message = jsonObject.getString("message");
                        callback.onFailure(message);
                        Log.d("", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
                Log.d("", "");
            }
        });
    }
}
