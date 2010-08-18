The KiWi Exporter stores the exported content and metadata in a specific 
binary format that is essentially a ZIP file with the following structure:
- a directory "items" containing XML representations of the content items
  contained in the export
- a directory "media" containing the binary media files and XML descriptors
  of the media metadata (filename, mime-type, ...)
- a directory "metadata" with an RDF/XML file containing the triple metadata
  of the export
  
  
The XML format for content items is as follows:
<content-item xmlns="http://www.kiwi-project.eu/kiwi/export/">
	<uri>URI of Resource</uri>
	<anon-id>Anonymous ID of Resource</anon-id>
	<title>Title of Content Item</title>
	<modified>...</modified>
	<created>...</created>
	<author>KiWi-ID of Author</author>
	<content>
	   XML Content of Content Item
	</content>
	<taggings>
	   <tagging>
	      <tagged-by>KiWi-ID of author</tagged-by>
	      <tagging-item>KiWi-ID of content item used as tag</tagging-item>
	      <tagging-date>...</tagging-date>
	   </tagging>
	   ...
	</taggings>
</content-item>

The XML format for the metadata of media content is as follows:
<media-content xmlns="http://www.kiwi-project.eu/kiwi/export/">
	<uri>URI of Resource</uri>
	<anon-id>Anonymous ID of Resource</anon-id>
	<file-name>The original filename of the media file</file-name>
	<mime-type>The MIME type of the media content</mime-type>
	<created>...</created>
</media-content>