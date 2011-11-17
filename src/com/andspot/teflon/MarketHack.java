package com.andspot.teflon;

import java.io.FileOutputStream;
import java.util.ArrayList;

import com.andspot.jsonk.JSONArray;
import com.andspot.jsonk.JSONException;
import com.gc.android.market.api.Base64;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market;
import com.gc.android.market.api.model.Market.AppType;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsRequest.OrderType;
import com.gc.android.market.api.model.Market.AppsRequest.ViewType;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.CommentsRequest;
import com.gc.android.market.api.model.Market.CommentsResponse;
import com.gc.android.market.api.model.Market.GetImageRequest;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;
import com.gc.android.market.api.model.Market.GetImageResponse;
import com.gc.android.market.api.model.Market.ResponseContext;


public class MarketHack {

	private int numS = 0;
	private String appID = "";
	
	public MarketHack(){
		
	}

	
	public String getAppID(final String username,final String password,final String packageName){
		MarketSession session = new MarketSession();
		session.login(username,password);
		session.getContext().setAndroidId("354957032521380");
		String query = packageName;
		AppsRequest appsRequest = AppsRequest.newBuilder().setQuery("pname:"+query) .setStartIndex(0).setEntriesCount(10).setWithExtendedInfo(true) .build();
		final ArrayList<String> apps = new ArrayList<String>();  
		
		session.append(appsRequest, new Callback<AppsResponse>() {
		         @Override
		         public void onResult(ResponseContext context, AppsResponse response) {
		        	 
			             for (int i = 0; i < response.getAppCount(); i++)
			             {
			                appID = response.getApp(i).getId();            
			                apps.add(appID);
			                apps.add(response.getApp(i).getVersion());
			                apps.add(response.getApp(i).getExtendedInfo().getInstallSize()+"");
			                apps.add(response.getApp(i).getVersionCode()+"");
        
			         }
		         }
		});
		session.flush();
		
		return  new JSONArray(apps).toString();
	
	}
	
	
	
	public ArrayList<String> getAppDetails(final String username,final String password,String packageName){
		MarketSession session = new MarketSession();
		
		
		session.login(username,password);
		session.getContext().setAndroidId("354957032521380");
		String query = packageName;
		AppsRequest appsRequest = AppsRequest.newBuilder().setQuery("pname:"+query) .setStartIndex(0).setEntriesCount(10).setWithExtendedInfo(true) .build();
		final ArrayList<String> apps = new ArrayList<String>();  
	
		session.append(appsRequest, new Callback<AppsResponse>() {
		         @Override
		         public void onResult(ResponseContext context, AppsResponse response) {
		        	   	String[] revar = new String[response.getAppCount()];
			             for (int i = 0; i < response.getAppCount(); i++)
			             {
			                appID = response.getApp(i).getId();
			        
			               String appName = response.getApp(i).getTitle();
			               String appRating = response.getApp(i).getRating();
			               String appPrice = response.getApp(i).getPrice();
			               String appCurrency = response.getApp(i).getPriceCurrency();
			               String devName = response.getApp(i).getCreator();
			               String devEmail = response.getApp(i).getExtendedInfo().getContactEmail();
			               String phone = response.getApp(i).getExtendedInfo().getContactPhone();
			               String website = response.getApp(i).getExtendedInfo().getContactWebsite();
			               String className = response.getApp(i).getPackageName();
			               String downloadString = response.getApp(i).getExtendedInfo().getDownloadsCountText();
			               numS = response.getApp(i).getExtendedInfo().getScreenshotsCount();
			               String[] temp = { appName, appRating, appPrice, appCurrency, devName, devEmail, className,phone,website,downloadString,String.valueOf(numS)};
			               
			               try {
							apps.add(new JSONArray(temp).toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			         }
		         }
		});
		session.flush();
		
		
		return  apps;
	
	}
	
	
	public ArrayList<String> getReviews(String username,String password,String appID){
		final ArrayList<String> apps = new ArrayList<String>();
		
		MarketSession session = new MarketSession();
		session.login(username,password);
		session.getContext().setAndroidId("354957032521380");
		 CommentsRequest commentsRequest = CommentsRequest.newBuilder()
                 .setAppId(appID)
                 .setStartIndex(0)
                 .setEntriesCount(10)
                 .build();
		
		session.append(commentsRequest, new Callback<CommentsResponse>() {
		@Override
		public void onResult(ResponseContext context, CommentsResponse response) {

			for(int i = 0;i<response.getCommentsCount();i++){
				String[] temp = {response.getComments(i).getAuthorName(),response.getComments(i).getText(),response.getComments(i).getRating()+"",response.getComments(i).getCreationTime()+""};
				try {
					System.out.println(new JSONArray(temp).toString());
					apps.add(new JSONArray(temp).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		});

		session.flush();
		return apps;
	}
	
	public String GetPaid(String username,String password,long offset,String category,int viewType,int orderType){
		ViewType vt = ViewType.PAID;
		OrderType ot = OrderType.NONE;
		if(viewType == 0){
			vt = ViewType.PAID;
		}else if(viewType == 1){
			vt = ViewType.FREE;
		}else if(viewType == 2){
			vt = ViewType.ALL;
		}
		
		if(orderType == 0){
			ot = OrderType.NONE;
		}else if(orderType == 1){
			ot = OrderType.NEWEST;
		}else if(orderType == 2){
			ot = OrderType.POPULAR;
		}else if(orderType == 3){
			ot = OrderType.FEATURED;
		}
		
		
		MarketSession session = new MarketSession();
		session.login(username,password);	
		session.getContext().setAndroidId("354957032521380");
		session.getContext().setDeviceAndSdkVersion("sapphire:7");
		Market.AppsRequest appsRequest;
		if(category == ""){
			appsRequest  = Market.AppsRequest.newBuilder().setStartIndex(offset).setAppType(AppType.GAME).setViewType(vt).setOrderType(ot).setEntriesCount(10).setWithExtendedInfo(true).build();
			
		}else{
			appsRequest = Market.AppsRequest.newBuilder().setStartIndex(offset).setAppType(AppType.GAME).setCategoryId(category).setViewType(vt).setOrderType(ot).setEntriesCount(10).setWithExtendedInfo(true).build();
			
		}
	
		final ArrayList<String> apps = new ArrayList<String>();     
		session.append(appsRequest, new Callback<AppsResponse>() {
		         @Override
		         public void onResult(ResponseContext context, AppsResponse response) {
		        	String[] revar = new String[response.getAppCount()];
		             for (int i = 0; i < response.getAppCount(); i++)
		             {
		               String appName = response.getApp(i).getTitle();
		               String appRating = response.getApp(i).getRating();
		               String appPrice = response.getApp(i).getPrice();
		               String appCurrency = response.getApp(i).getPriceCurrency();
		               String devName = response.getApp(i).getCreator();
		               String devEmail = response.getApp(i).getExtendedInfo().getContactEmail();
		               String phone = response.getApp(i).getExtendedInfo().getContactPhone();
		               String website = response.getApp(i).getExtendedInfo().getContactWebsite();
		               String className = response.getApp(i).getPackageName();
		               String downloadString = response.getApp(i).getExtendedInfo().getDownloadsCountText();
		               String[] temp = { appName, appRating, appPrice, appCurrency, devName, devEmail, className,phone,website,downloadString};
		               
		               try {
						apps.add(new JSONArray(temp).toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		         }
		         }
		});
		session.flush();
	
		return new JSONArray(apps).toString();
	}
}