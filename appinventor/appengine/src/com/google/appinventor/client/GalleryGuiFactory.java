package com.google.appinventor.client;

import java.util.List;

import com.google.appinventor.client.output.OdeLog;
import com.google.appinventor.shared.rpc.project.GalleryApp;
import com.google.appinventor.shared.rpc.project.GalleryComment;
import com.google.appinventor.shared.rpc.project.UserProject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class GalleryGuiFactory implements GalleryRequestListener {
	GalleryClient gallery = null;

	public GalleryGuiFactory() {
		gallery = new GalleryClient(this);
	}

  private class GalleryAppWidget {
    final Label nameLabel;
    final Label authorLabel;
    final Label numDownloadsLabel;
    final Label numCommentsLabel;
    final Label numViewsLabel;
    final Label numLikesLabel;
    final Image image;
  
    private GalleryAppWidget(final GalleryApp app) {
      nameLabel = new Label(app.getTitle());
      nameLabel.addStyleName("ode-ProjectNameLabel");
  
      authorLabel = new Label(app.getDeveloperName());
      numDownloadsLabel = new Label(Integer.toString(app.getDownloads()));
      numLikesLabel = new Label(Integer.toString(app.getLikes()));
      numViewsLabel = new Label(Integer.toString(app.getViews()));
      numCommentsLabel = new Label(Integer.toString(app.getComments()));
      image = new Image();
      image.setUrl(app.getImageURL());
      
    }
  }
	
  public Button generateSearchPanel(FlowPanel container) {
    TextBox sta = new TextBox();
    sta.addStyleName("gallery-search-textarea");
    Button sb = new Button("Search for apps");
    sb.addStyleName("app-action");
    container.add(sta);
    container.add(sb);
    container.addStyleName("gallery-search");
    return sb;
  }
  
	public void generateHorizontalCards(List<GalleryApp> apps, FlowPanel container, Boolean refreshable) {
    if (refreshable) {
      // Flood the panel's content if we knew new stuff is coming in!
      container.clear();
    }
		for (final GalleryApp app : apps) {
		  // Create the associated GUI object for app
		  GalleryAppWidget gaw = new GalleryAppWidget(app);
		  
		  // Create necessary GUI wrappers and components
      FlowPanel appCard = new FlowPanel();
      FlowPanel appCardContent = new FlowPanel();
      FlowPanel appCardMeta = new FlowPanel();
      
      // Special processing for the app title, mainly for fade-out effect
      HTML appTitle = new HTML("" +
        "<div class='gallery-title'>" + gaw.nameLabel.getText() +
        "<span class='paragraph-end-block'></span></div>");

      gaw.image.addClickHandler(new ClickHandler() {
      //  @Override
        public void onClick(ClickEvent event) {
          Ode.getInstance().switchToGalleryAppView(app); 
        }
      });
      
      appTitle.addClickHandler(new ClickHandler() {
      //  @Override
        public void onClick(ClickEvent event) {
          Ode.getInstance().switchToGalleryAppView(app); 
        }
      });

      // Add everything to the top-level stuff
      appCard.add(gaw.image);
      appCard.add(appCardContent);
      appCardContent.add(appTitle);
      appCardContent.add(gaw.authorLabel);
      appCardContent.add(appCardMeta);

      // Set helper icons
      Image numViews = new Image();
      numViews.setUrl("http://i.imgur.com/jyTeyCJ.png");
      Image numDownloads = new Image();
      numDownloads.setUrl("http://i.imgur.com/j6IPJX0.png");
      Image numLikes = new Image();
      numLikes.setUrl("http://i.imgur.com/N6Lpeo2.png");
    // For generic cards, do not show comment
//    Image numComments = new Image();
//    numComments.setUrl("http://i.imgur.com/GGt7H4c.png");
      
      appCardMeta.add(numViews);
      appCardMeta.add(gaw.numViewsLabel);
      appCardMeta.add(numDownloads);
      appCardMeta.add(gaw.numDownloadsLabel);
      appCardMeta.add(numLikes);
      appCardMeta.add(gaw.numLikesLabel);
      // For generic cards, do not show comment
//      appCardMeta.add(numComments);
//      appCardMeta.add(gaw.numCommentsLabel);
      
      // Add associated styling
      appCard.addStyleName("gallery-card");
      gaw.image.addStyleName("gallery-card-cover");
//      gaw.nameLabel.addStyleName("gallery-title");
      gaw.authorLabel.addStyleName("gallery-subtitle");
      appCardContent.addStyleName("gallery-card-content");
      gaw.numViewsLabel.addStyleName("gallery-meta");
      gaw.numDownloadsLabel.addStyleName("gallery-meta");
      gaw.numLikesLabel.addStyleName("gallery-meta");
//      gaw.numCommentsLabel.addStyleName("gallery-meta");
      
      container.add(appCard);
		}
		container.addStyleName("gallery-app-collection");
		
	}

  @Override
  public void onAppListRequestCompleted(List<GalleryApp> apps, int requestID) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onCommentsRequestCompleted(List<GalleryComment> comments) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onSourceLoadCompleted(UserProject projectInfo) {
    // TODO Auto-generated method stub
    
  }
	
	
}