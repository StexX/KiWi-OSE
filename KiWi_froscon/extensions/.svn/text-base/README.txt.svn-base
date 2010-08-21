README for KiWi Extensions
=======================

Technical details: see http://www.seamframework.org/Community/SeamPlugins


This directory contains KiWi Extensions. A extension can contain actions, services, perspectives, 
and widgets.

- an "action" is a functionality that can be registered for use with ContentItems.
  NB: the "action" mechanism is not yet available, but foreseen for the future
- a "service" is a (collection of) stateless session beans that provide certain
  service functionality (i.e. Java methods) to other components
- a "perspective" is a complete layout specification with xhtml files, css files,
  JavaScript files, and accompanying action beans (view backing beans); for example,
  there is a perspective "TagIT" and a perspective "Wiki".
- a "widget" is a small user interface component that can be used by different perspectives 
  to show or edit a certain kind of information, e.g. EXIF metadata, tagging, ...
  
Each of the different types can contain Seam or EJB components that implement their own
initialisation methods if needed. If an extension needs considerable initialisation effort, it is
recommended to provide a separate service e.g. called ExamplePluginInit that provides
the functionality for initialisation. Extensions should register themselves via the 
extensionService component (API: kiwi.api.extension.ExtensionService) so that the core system
can include them in the appropriate parts of the user interface.
  
  
For each plugin, there is a separate subdirectory beneath this directory. The structure is as follows:

extensions
+-- exampleapplication
    +-- resources
        +-- META-INF
            +-- components.xml
            +-- faces-config.xml
        +-- messages_en.properties
    +-- src
        +-- exampleplugin
            +-- action
                +-- ...
        	+-- api
                +-- ...
        	+-- services
        		+-- ...
    +-- view
        +-- javascript
            +-- exampleplugin.js
    	+-- layout
    		+-- template.xhtml
        +-- stylesheet
            +-- exampleplugin.css
        +-- file1.xhtml
        +-- file2.xhtml
        +-- ...
     		
     		
When adding a new extension, you additionally need to update the files build.xml (adding the
name of the extension to the property "extensions") and resources/META-INF/application.xml 
(adding a new EJB definition pointing to the file kiwiext-EXTNAME.jar).   		
        