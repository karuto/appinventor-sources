// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

package com.google.appinventor.client.boxes;

import static com.google.appinventor.client.Ode.MESSAGES;
import com.google.appinventor.client.explorer.youngandroid.ProjectList;
import com.google.appinventor.client.explorer.youngandroid.ProjectToolbar;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TabPanel;


/**
 * TabPanel implementation for project list.
 *
 */
public final class ProjectListBox extends TabPanel {

  // Singleton project explorer box instance (only one project explorer allowed)
  private static final ProjectListBox INSTANCE = new ProjectListBox();
  
  // Project list for young android
  private final ProjectList plist;
  
  private ProjectToolbar projectToolbar;
  

  /**
   * Returns the singleton projects list box.
   *
   * @return  project list box
   */
  public static ProjectListBox getProjectListBox() {
    //INSTANCE.setToolbar(pt);
    return INSTANCE;
  }
  
  private void setToolbar(ProjectToolbar pt) {
    
  }
  
  /**
   * Creates new project list box.
   */
  private ProjectListBox() {
    /*
    super(MESSAGES.projectListBoxCaption(),
        300,    // height
        false,  // minimizable
        false); // removable
    */
    plist = new ProjectList();
    
//    projectToolbar = new ProjectToolbar();
    FlowPanel pContainer = new FlowPanel();
//    pContainer.add(projectToolbar);
    pContainer.add(plist);
    FlowPanel sContainer = new FlowPanel();
    
    this.add(pContainer, MESSAGES.projectListBoxCaption());
    this.add(sContainer, "My Studios");
    this.selectTab(0);
    
    // Styling options
    this.addStyleName("ode-MyTabs");
  }

  /**
   * Returns project list associated with projects explorer box.
   *
   * @return  project list
   */
  public ProjectList getProjectList() {
     return plist;
  }
}
