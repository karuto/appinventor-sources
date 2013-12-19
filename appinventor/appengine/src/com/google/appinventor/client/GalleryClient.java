package com.google.appinventor.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import com.google.appinventor.client.Ode;
import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.output.OdeLog;
import com.google.appinventor.client.wizards.NewProjectWizard.NewProjectCommand;
import com.google.appinventor.shared.rpc.project.UserProject;
import com.google.appinventor.client.youngandroid.TextValidators;

import com.google.appinventor.shared.rpc.project.GalleryApp;
import com.google.appinventor.shared.rpc.project.GalleryComment;

import java.io.IOException;

//import java.net.URLEncoder;
// import java.io.UnsupportedEncodingException;


public class GalleryClient {

  private GalleryRequestListener listener;
  public static final int REQUEST_FEATURED=1;
  public static final int REQUEST_RECENT=2;
  public static final int REQUEST_SEARCH=3;
  public static final int REQUEST_MOSTLIKED=4;
  public static final int REQUEST_MOSTDOWNLOADED=5;
  public static final int REQUEST_MOSTVIEWED=6;
  public static final int REQUEST_BYDEVELOPER=7;
  public static final int REQUEST_BYTAG=8;
  public static final int REQUEST_ALL=9;

  public GalleryClient(GalleryRequestListener listener) {
    this.listener=listener;
  }
  
  public void FindApps(String keywords, int start, int count, int sortOrder) {
  // we need to deal with URL encoding of keywords
  requestApps("http://gallery.appinventor.mit.edu/rpc?tag=search:"+keywords+
      getStartCountString(start,count),REQUEST_SEARCH);		
  }

  public void FindByTag(String tag, int start, int count, int sortOrder) {
    // need to deal with URL encoding
    requestApps("http://gallery.appinventor.mit.edu/rpc?tag=tag:"+tag+ 
       getStartCountString(start,count), REQUEST_BYTAG);
  }
  // must have start and count, with filter after
  public void GetApps(int start, int count, int sortOrder, String sortField) {
    // currently returns five apps sorted by uid (app id)
    requestApps("http://gallery.appinventor.mit.edu/rpc?tag=all"+ 
      getStartCountString(start,count)+":asc:uid",REQUEST_ALL);
  }

  public void GetAppsByDeveloper(int start, int count, String developerName) {
    // this works, sample is:http://gallery.appinventor.mit.edu/rpc?tag=by_developer:o21b3f
    // NOTE: do need to worry about url encoding because displayName, which is the
    //   field we stick in as a parameter, can have spaces. For instance, my displayName
    //   is David Wolber
    
    // lets try a kludge that only handles spaces
    String encodedDevName=developerName.replace(" ","%20");
    requestApps("http://gallery.appinventor.mit.edu/rpc?tag=by_developer:"+
        encodedDevName+getStartCountString(start,count), REQUEST_BYDEVELOPER);
  }

  public void GetCategories() {
     // we need another way to deal with non-list returns
     //"http://gallery.appinventor.mit.edu/rpc?tag=get_categories"
  }

  public void GetFeatured(int start, int count, int sortOrder) {
    requestApps("http://gallery.appinventor.mit.edu/rpc?tag=featured"+
       getStartCountString(start,count), REQUEST_FEATURED);
  }
  // uploadTime,desc gets most recent uploaded of source, 
  // creationTime would give time project was first added to gallery
  public void GetMostRecent(int start, int count) {
    OdeLog.log("at getMostRecent in client");
    int STATUS_CODE_OK = 200;  
    // Callback for when the server returns us the apps
    final Ode ode = Ode.getInstance();
    final OdeAsyncCallback<List<GalleryApp>> callback = new OdeAsyncCallback<List<GalleryApp>>(
    // failure message
    MESSAGES.galleryError()) {
    @Override
    public void onSuccess(List<GalleryApp> apps) {
      // the server has returned us something
      OdeLog.log("received recent gallery apps successfully"+apps.size());
      OdeLog.log("first app title"+apps.get(0).getTitle());
      listener.onAppListRequestCompleted(apps, REQUEST_RECENT); 
    }
    };
      
    // ok, this is below the call back, but of course it is done first 
    ode.getGalleryService().getRecentApps(0,5,callback);
  
  /*  requestApps("http://gallery.appinventor.mit.edu/rpc?tag=all"+
       getStartCountString(start,count)+":desc:uploadTime", REQUEST_RECENT); */
  }
  
  public void GetMostDownloaded(int start, int count) {
    
    requestApps("http://gallery.appinventor.mit.edu/rpc?tag=all"+
       getStartCountString(start,count)+":desc:numDownloads",REQUEST_MOSTDOWNLOADED);
  }

  public void GetMostViewed(int start, int count) {
    //doesn't work, need Vince to add index
    requestApps("http://gallery.appinventor.mit.edu/rpc?tag=all"+
       getStartCountString(start,count)+":desc:numViewed", REQUEST_MOSTVIEWED);
  }

  public void GetMostLiked(int start, int count) {
    requestApps("http://gallery.appinventor.mit.edu/rpc?tag=all"+
       getStartCountString(start,count)+":desc:numLikes", REQUEST_MOSTLIKED);
  }
	
  public void GetProject(String id) {
    // we need another way to deal with non-list things 
    //"http://gallery.appinventor.mit.edu/rpc?tag=getinfo:"+id);
  }
  // http://gallery.appinventor.mit.edu/rpc?tag=comments:uid
  // sample: http://gallery.appinventor.mit.edu/rpc?tag=comments:111004
  public void GetComments(String appId,int start,int count)
  {
    requestComments("http://gallery.appinventor.mit.edu/rpc?tag=comments:"+appId+
       getStartCountString(start,count));
  }
  public void Publish(GalleryApp app) {
    // TODO Auto-generated method stub
  }

  public void Update(GalleryApp app) {
    // TODO Auto-generated method stub
  }



  private void requestApps(String url, final int requestId)  {
   /*
    int STATUS_CODE_OK = 200;  

    // Callback for when the server returns us the apps
    final Ode ode = Ode.getInstance();
    final OdeAsyncCallback<List<GalleryApp>> callback = new OdeAsyncCallback<List<GalleryApp>>(
      // failure message
      MESSAGES.galleryError()) {
      @Override
      public void onSuccess(List<GalleryApp> list) {
        // the server has returned us something
    	  OdeLog.log("#############################");
        if (list== null) {
          return;
        }
        // things are good so tell the ui listener
        listener.onAppListRequestCompleted(list, requestId);   
      }
    };
    // ok, this is below the call back, but of course it is done first 
    ode.getProjectService().getApps(url, callback);
    */
  }

  private void requestComments(String url) {
    /*
    
    // Callback for when the server returns us the apps
    final Ode ode = Ode.getInstance();
    final OdeAsyncCallback<List<GalleryComment>> callback = new OdeAsyncCallback<List<GalleryComment>>(
      // failure message
      MESSAGES.galleryError()) {
      @Override
      public void onSuccess(List<GalleryComment> list) {
        // the server has returned us something
        if (list== null) {
          return;
        }
        // things are good so tell the ui listener
        listener.onCommentsRequestCompleted(list);   
      }
    };
    // ok, this is below the call back, but of course it is done first 
    ode.getProjectService().getComments(url, callback);

    */
  }	
 // public void loadSourceFile(final String projectName, String sourceURL, long galleryId) {
  public void loadSourceFile(GalleryApp gApp) {
    final String projectName=gApp.getProjectName();
    final String sourceURL=gApp.getSourceURL();
    final long galleryId = gApp.getGalleryAppId();
    
    // first check name to see if valid and unique...
    if (!TextValidators.checkNewProjectName(projectName))
      return;  // the above function takes care of error messages
    // Callback for updating the project explorer after the project is created on the back-end
    final Ode ode = Ode.getInstance();

    final OdeAsyncCallback<Void> galleryCallback = new OdeAsyncCallback<Void>(	      
    // failure message
    MESSAGES.createProjectError()) {
      @Override
      public void onSuccess(Void arg2) {
      }
    };
    
    final OdeAsyncCallback<UserProject> callback = new OdeAsyncCallback<UserProject>(	      
    // failure message
    MESSAGES.createProjectError()) {
      @Override
      public void onSuccess(UserProject projectInfo) {
        // if we were able to create the new project, lets increment download count in gallery db
        ode.getGalleryService().appWasDownloaded(galleryId,projectInfo.getProjectId(),galleryCallback);

        // now relay the result back to UI client
        listener.onSourceLoadCompleted(projectInfo);    
      }
    };
    // this is really what's happening here, we call server to load project
    ode.getProjectService().newProjectFromGallery(projectName, sourceURL, galleryId, callback);
  } 
 
 

  private String getStartCountString(int start, int count) {
    return ":"+String.valueOf(start)+":"+String.valueOf(count);  
  }
/*    
  private String getEncoded(String param)
  {
    try {
	  String result = URLEncoder.encode(param, "UTF-8");
      return result;
	}
	catch (UnsupportedEncodingException e)
	{
		// need to do something here
        return param;
	}
  }
*/
}
