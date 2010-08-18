package kiwi.view.vmt.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kiwi.transport.client.TransportSkosConcept;
import kiwi.transport.client.TransportSkosDetails;
import kiwi.transport.client.TransportTag;
import kiwi.transport.client.TransportSkosDetails.TransportLanguageDetails;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.SubmitValuesEvent;
import com.smartgwt.client.widgets.form.events.SubmitValuesHandler;
import com.smartgwt.client.widgets.form.fields.BlurbItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SubmitItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class VMT implements EntryPoint {
	static class SkosConcept extends TreeNode {
		public SkosConcept(TransportSkosConcept aTransport)
		{
			setAttribute("dbId", aTransport.id);
			setAttribute("label", aTransport.label);
			if(!aTransport.hasChildren)
				setAttribute("children", new SkosConcept[0]);
			setAttribute("isScheme", aTransport.isScheme);
			if(aTransport.isScheme)
				setCanDrag(false);
			setAttribute("id", idCache.size());
			idCache.add(this);
		}
	}
	static class FreeTag extends ListGridRecord {
		public FreeTag(TransportTag aTag)
		{
			setAttribute("dbId", aTag.id);
			setAttribute("tag", aTag.label);
			setAttribute("id", idCache.size());
			idCache.add(this);
		}
	}

	/**
	 * This is the main datasource responsible for holding the data that is
	 * displayed in VMTs tree view. It is basically responsible for fetching
	 * data from the backend server, to convert dropped tags to concepts
	 * and to move/copy concepts around in the tree.
	 * It is not a strict "model" component as it does create some GUI
	 * components during its operation.
	 * 
	 * @see http://code.google.com/p/smartgwt-extensions/source/browse/trunk/src/main/java/com/smartgwt/extensions/client/gwtrpcds/GwtRpcDataSource.java?r=14
	 */
	static class SKOSData extends DataSource {
		public SKOSData()
		{
			setDataProtocol(DSProtocol.CLIENTCUSTOM);
			setDataFormat(DSDataFormat.CUSTOM);
			setClientOnly(false);
			
			setTitleField("label");
			DataSourceIntegerField idField = new DataSourceIntegerField("id", "id");
			idField.setPrimaryKey(true);
			idField.setRequired(true);
			DataSourceIntegerField parentField = new DataSourceIntegerField("parentId", "parentId");
			parentField.setForeignKey("id");
			parentField.setRootValue(0);
			DataSourceTextField labelField = new DataSourceTextField("label", "Label");
			
			setFields(idField, labelField, parentField);
		}
		@Override
		protected Object transformRequest(final DSRequest request) {
			final String requestId = request.getRequestId();
			final DSResponse response = new DSResponse();
			response.setAttribute("clientContext", request.getAttributeAsObject ("clientContext"));
			response.setStatus(0);
			
			switch(request.getOperationType()) {
				case FETCH:
					executeFetch(requestId, request, response);
					break;
				case ADD:
					executeAdd(requestId, request, response);
					break;
				case UPDATE:
					executeUpdate(requestId, request, response);
					break;
				case REMOVE:
					executeRemove(requestId, request, response);
					break;
				default:
					// Operation not implemented.
					break;
			}
			return request.getData();
		}
		private void executeRemove(final String requestId, final DSRequest request,
				final DSResponse response) {
			//response.setStatus(DSResponse.STATUS_FAILURE);
			processResponse(requestId, response);
		}
		private void executeUpdate(final String requestId, final DSRequest request,
				final DSResponse response) {
			final TreeNode rec = new TreeNode(request.getData());
			final int oldParentId = idCache.get(request.getOldValues().getAttributeAsInt("parentId")).getAttributeAsInt("dbId");
			final int parentId = idCache.get(rec.getAttributeAsInt("parentId")).getAttributeAsInt("dbId");
			if(parentId == 0)
			{
				SC.say("Can't move Concepts to the root");
				response.setStatus(DSResponse.STATUS_FAILURE);
				processResponse(requestId, response);
				return;
			}
			
			final Window w = new Window();
			w.setTitle("Move or Copy?");
			w.setIsModal(true);
			w.setShowMinimizeButton(false);
			w.centerInPage();
			w.setWidth(350);
			w.setHeight(100);
			w.addCloseClickHandler(new CloseClickHandler() {
				@Override
				public void onCloseClick(CloseClientEvent event) {
					response.setStatus(DSResponse.STATUS_FAILURE);
					processResponse(requestId, response);
					w.destroy();
				}
			});
			
			Label l = new Label("Selecting copy adds this as another parent to the concept"+
					", making it appear in both subtrees."); 
			l.setAutoHeight();
			l.setPadding(5);
			
			w.addItem(l);
			
			HLayout buttons = new HLayout();
			
			IButton copy = new IButton("Copy");
			copy.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Record[] c =  rec.getAttributeAsRecordArray("children");
					Record newRec = new SkosConcept(new TransportSkosConcept(
							rec.getAttributeAsInt("dbId"),
							rec.getAttribute("label"),
							c != null ? (c.length > 0) : true,
							false));
					newRec.setAttribute("parentId", rec.getAttributeAsInt("parentId"));
					skosTree.addData(newRec);
					
					// it didn't really fail, but this is needed so the tree
					// does not automatically move the concept
					response.setStatus(DSResponse.STATUS_FAILURE);
					processResponse(requestId, response);

					// wow, passing null as AsyncCallback isn't allowed...
					// hooray for unnecessary code duplication
					service.copyConcept(rec.getAttributeAsInt("dbId"), parentId, emptyBoolCallback);
					w.destroy();
				}
			});

			IButton move = new IButton("Move");
			move.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// clear the child data so it is reloaded correctly
					JSOHelper.deleteAttribute(rec.getJsObj(), "children");
					response.setData(new TreeNode[] { rec });
					processResponse(requestId, response);

					service.moveConcept(rec.getAttributeAsInt("dbId"), oldParentId, parentId, emptyBoolCallback);
					w.destroy();
				}
			});
			
			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					response.setStatus(DSResponse.STATUS_FAILURE);
					processResponse(requestId, response);
					w.destroy();
				}
			});
			
			buttons.setMembers(new LayoutSpacer(), copy, move, cancel);
			
			w.addItem(buttons);
			
			w.show();
		}
		private void executeAdd(final String requestId, final DSRequest request,
				final DSResponse response) {
			final TreeNode rec = new TreeNode(request.getData());
			
			if(rec.getAttribute("label") != null) // add new Scheme/Concept
			{
				response.setData(new TreeNode[] { rec });
				processResponse(requestId, response);
				return;
			}
			
			final int parentId = idCache.get(rec.getAttributeAsInt("parentId")).getAttributeAsInt("dbId");
			if(parentId == 0)
			{
				SC.say("Can't move Tags to the root");
				freeGrid.addData(rec); // add it back to the tag list
				response.setStatus(DSResponse.STATUS_FAILURE);
				processResponse(requestId, response);
				return;
			}
			// else
			rec.setAttribute("label", rec.getAttribute("tag"));
			rec.setAttribute("children", new SkosConcept[0]);
			response.setData(new TreeNode[] { rec });
			processResponse(requestId, response);
			
			service.convertToConcept(rec.getAttributeAsInt("dbId"), parentId, emptyBoolCallback);
		}
		public void executeFetch(final String requestId, final DSRequest request,
				final DSResponse response) {

			service.getChildConcepts(idCache.get(request.getCriteria().getAttributeAsInt("parentId")).getAttributeAsInt("dbId"), new AsyncCallback<List<TransportSkosConcept>>() {
				public void onFailure(Throwable t) {
					response.setStatus(DSResponse.STATUS_FAILURE);
					processResponse(requestId, response);
				}

				@Override
				public void onSuccess(List<TransportSkosConcept> result) {
					SkosConcept[] l = new SkosConcept[result.size()];
					for(int i = 0; i < result.size(); i++)
						l[i] = new SkosConcept(result.get(i));
					response.setData(l);
					response.setTotalRows(result.size());
					processResponse(requestId, response);
				}
			});
		}
	}

	/*static Canvas output;
	public static void log(String aLog)
	{
		output.setContents(output.getContents()+"<br/>"+aLog);
	}
	public static void log(Record aRec)
	{
		String[] rs = aRec.getAttributes();
		for(String s : rs)
			log(s+": "+aRec.getAttribute(s));
	}*/

	// wow, java really does suck, no doubt about it
	public static String join(String aDelim, Collection<String> aElems)
	{
		StringBuilder builder = new StringBuilder();
		for(String s : aElems)
			builder.append(s+aDelim);
		return builder.substring(0, builder.length()-aDelim.length());
	}
	
	public static List<String> split(String aDelim, String aStr)
	{
		String[] strs = aStr.split(";");
		List<String> res = new ArrayList<String>(strs.length);
		for(String s : strs)
		{
			res.add(s.trim());
		}
		return res;
	}

	// hahaha, thats funny, can't even use primitive types as template arguments
	static Map<Long, TransportSkosDetails> detailsCache = new HashMap<Long, TransportSkosDetails>();
	static TransportSkosDetails nowEditing = null;
	
	static VMTServerAsync service;
	static List<Record> idCache = new ArrayList<Record>(8);
	
	// passing null as callback parameter would be a lot easier, but the API
	// docs clearly say that passing null is not allowed.
	static AsyncCallback<Boolean> emptyBoolCallback = new AsyncCallback<Boolean>() {
		public void onFailure(Throwable t) {
		}

		@Override
		public void onSuccess(Boolean result) {
		}
	};

	// some GUI elements that need to be accessible throughout the app.
	static TreeGrid skosTree;
	static ListGrid freeGrid;
	static VLayout leftPane;
	static TabSet langTabs;

	public static void saveDetails()
	{
		if(nowEditing != null)
		{
			Tab[] tabs = langTabs.getTabs();
			for(int i = 0; i < tabs.length; i++)
			{
				Tab t = tabs[i];
				String lang = t.getTitle();
				DynamicForm form = (DynamicForm) t.getPane();
				Record values = form.getValuesAsRecord();
				String prefLabel = values.getAttribute("prefLabel");
				prefLabel = prefLabel == null ? "" : prefLabel;
				String altLabels = values.getAttribute("altLabels");
				altLabels = altLabels == null ? "" : altLabels;
				String hiddenLabels = values.getAttribute("hiddenLabels");
				hiddenLabels = hiddenLabels == null ? "" : hiddenLabels;
				String definition = values.getAttribute("definition");
				definition = definition == null ? "" : definition;
				if(prefLabel.isEmpty() && altLabels.isEmpty() && hiddenLabels.isEmpty() && definition.isEmpty())
					nowEditing.languages.remove(lang);
				else
				{
					TransportLanguageDetails l = nowEditing.languages.get(lang);
					l.prefLabel = prefLabel;
					l.altLabels.clear();
					l.altLabels.addAll(split(";", altLabels));
					l.hiddenLabels.clear();
					l.hiddenLabels.addAll(split(";", hiddenLabels));
					l.definition = definition;
				}
			}
		}
	}
	
	public static Tab langTab(String aLang, TransportLanguageDetails aDetails)
	{
		Tab tab = new Tab(aLang);
		DynamicForm form = new DynamicForm();
		form.setSaveOnEnter(true);
		form.setColWidths("120", "*");
		
		TextItem prefLabel = new TextItem("prefLabel", "Preferred Label");
		prefLabel.setWidth("*");
		prefLabel.setValue(aDetails.prefLabel);
		
		TextItem altLabels = new TextItem("altLabels", "Alternative Labels*");
		altLabels.setWidth("*");
		altLabels.setValue(join("; ", aDetails.altLabels));

		TextItem hiddenLabels = new TextItem("hiddenLabels", "Hidden Labels*");
		hiddenLabels.setWidth("*");
		hiddenLabels.setValue(join("; ", aDetails.hiddenLabels));
		
		BlurbItem hint = new BlurbItem();
		hint.setDefaultValue("* separated by semicolon");
		
		TextAreaItem definition = new TextAreaItem("definition");
		definition.setShowTitle(false);
		definition.setColSpan(2);
		definition.setWidth("*");
		definition.setValue(aDetails.definition);
		
		form.setFields(prefLabel, altLabels, hiddenLabels, hint, definition);
		
		tab.setPane(form);
		return tab;
	}
	public static void refreshDetails(TransportSkosDetails aDetails)
	{
		// temporarily save the details
		saveDetails();
		nowEditing = aDetails;
		langTabs = new TabSet();
		
		for(Entry<String, TransportLanguageDetails> e : aDetails.languages.entrySet())
		{
			langTabs.addTab(langTab(e.getKey(), e.getValue()));
		}
		
		DynamicForm newform = new DynamicForm();
		newform.setSaveOnEnter(true);
		
		final SubmitItem newlangsubmit = new SubmitItem("submitNewLang", "Add language");
		newlangsubmit.setDisabled(true);
		
		final TextItem newlang = new TextItem("newLang", "New Language");
		newlang.setRequired(true);
		newlang.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				newlangsubmit.setDisabled(event.getValue().toString().isEmpty());
			}
		});
		
		newform.setFields(newlang, newlangsubmit);
		newform.addSubmitValuesHandler(new SubmitValuesHandler() {
			@Override
			public void onSubmitValues(SubmitValuesEvent event) {
				String lang = (new Record(event.getValues())).getAttribute("newLang");
				if(lang.isEmpty() || nowEditing.languages.containsKey(lang))
					return;
				TransportLanguageDetails details = new TransportLanguageDetails(true);
				nowEditing.languages.put(lang, details);
				langTabs.addTab(langTab(lang, details));
				newlang.setValue("");
				newlangsubmit.setDisabled(true);
			}
		});
		
		final ListGrid relatedGrid = new ListGrid();

		final IButton delrelation = new IButton("Remove relationship to selected Concepts");
		delrelation.setDisabled(true);
		delrelation.setAutoFit(true);
		delrelation.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<Long> l = new ArrayList<Long>(8);
				for(final Record rec : relatedGrid.getSelection())
				{
					final TransportSkosDetails editing = nowEditing; // nowEditing might have changed until the server reply arrives
					final int recId = idCache.get(rec.getAttributeAsInt("id")).getAttributeAsInt("dbId");
					
					TransportSkosDetails other = detailsCache.get(new Long(recId));
					if(other != null)
					{
						other.related.remove(new TransportSkosConcept(editing.id, editing.label, false, false));
					}
					editing.related.remove(new TransportSkosConcept(recId, rec.getAttribute("label"), false, false));
					relatedGrid.removeData(rec);
					
					delrelation.setDisabled(true);
					l.add(new Long(recId));
				}
				/*
				 * Now this really is EPIC FAIL:
				 * calling this concurrently results in a RollbackException, whatever the
				 * hell that should be.
				 * Try to work around it by grouping it together and sending just one
				 * request to the server. It may still fail if more than one person
				 * works on this concurrently.
				 */
				service.unsetRelated(nowEditing.id, l, emptyBoolCallback);
			}
		});
		
		relatedGrid.setCanAcceptDroppedRecords(true);
		relatedGrid.setFields(new ListGridField("label", "Concept"));
		for(TransportSkosConcept c : nowEditing.related)
			relatedGrid.addData(new SkosConcept(c));
		relatedGrid.addRecordDropHandler(new RecordDropHandler() {
			@Override
			public void onRecordDrop(final RecordDropEvent event) {
				event.cancel();
				final Record rec = event.getDropRecords()[0];
				if(rec.getAttribute("label") == null) // this must be a free tag dropped here
					return;
				final TransportSkosDetails editing = nowEditing; // nowEditing might have changed until the server reply arrives
				final int recId = idCache.get(rec.getAttributeAsInt("id")).getAttributeAsInt("dbId");
				final TransportSkosConcept newEntry = new TransportSkosConcept(recId, rec.getAttribute("label"), false, false);
				if(editing.related.contains(newEntry))
					return;
				
				TransportSkosDetails other = detailsCache.get(new Long(recId));
				if(other != null)
				{
					other.related.add(new TransportSkosConcept(editing.id, editing.label, false, false));
				}
				editing.related.add(newEntry);
				relatedGrid.addData(rec);
				
				service.setRelated(recId, editing.id, emptyBoolCallback);
			}
		});
		relatedGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				delrelation.setDisabled(relatedGrid.getSelection().length == 0);
			}
		});
		
		SectionStack sections = new SectionStack();
		sections.setVisibilityMode(VisibilityMode.MULTIPLE);
		
		SectionStackSection rel = new SectionStackSection("Relationships");
		rel.setExpanded(true);
		rel.setCanCollapse(false);
		rel.setControls(delrelation);
		Label l = new Label("Drag any Concept from the SKOS Tree here to set the Concepts related.");
		l.setAutoHeight();
		l.setPadding(5);
		rel.addItem(l);

		rel.addItem(relatedGrid);
		sections.addSection(rel);
		
		SectionStackSection lang = new SectionStackSection("Languages");
		lang.setExpanded(true);
		lang.setCanCollapse(false);
		lang.addItem(newform);
		IButton savedetails = new IButton("Save language details");
		savedetails.setAutoFit(true);
		lang.setControls(savedetails);
		savedetails.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveDetails();
				service.saveDetails(nowEditing, emptyBoolCallback);
			}
		});
		lang.addItem(langTabs);
		sections.addSection(lang);
		
		leftPane.setMembers(sections);
	}

	@Override
	public void onModuleLoad() {
		String endpointURL = GWT.getModuleBaseURL() + "../../seam/resource/gwt";
		service = GWT.create(VMTServer.class);
		((ServiceDefTarget) service).setServiceEntryPoint(endpointURL);

		Record root = new Record();
		root.setAttribute("dbId", 0);
		idCache.add(root);
		
		skosTree = new TreeGrid() {
			protected String getCellCSSText(ListGridRecord record, int rowNum,
					int colNum) {
				if(record.getAttributeAsBoolean("isScheme"))
					return "font-weight: bold;";
				return super.getCellCSSText(record, rowNum, colNum);
			}
		};
		skosTree.setShowConnectors(true);
		skosTree.setCanAcceptDroppedRecords(true);
		skosTree.setCanReparentNodes(true);
		skosTree.setCanDragRecordsOut(true);
		skosTree.setDataSource(new SKOSData());
		skosTree.setAutoFetchData(true);
		skosTree.setFields(new TreeGridField("label", "Concepts"));
		skosTree.setSortField("label");
		skosTree.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record rec = event.getRecord();
				if(rec.getAttributeAsBoolean("isScheme"))
					return;
				long id = idCache.get(rec.getAttributeAsInt("id")).getAttributeAsInt("dbId");
				if(detailsCache.containsKey(new Long(id)))
				{
					refreshDetails(detailsCache.get(new Long(id)));
					return;
				}
				
				service.getDetails(id, new AsyncCallback<TransportSkosDetails>() {
					public void onFailure(Throwable t) {
					}

					@Override
					public void onSuccess(TransportSkosDetails result) {
						detailsCache.put(new Long(result.id), result);
						refreshDetails(result);
					}
				});
			}
		});
		
		final IButton newConcept = new IButton("New Concept");
		newConcept.setAutoFit(true);
		newConcept.setDisabled(true);
		newConcept.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final int parentId = skosTree.getSelectedRecord().getAttributeAsInt("id");
				final int parentDbId = idCache.get(parentId).getAttributeAsInt("dbId");
				SC.askforValue("Preferred Label (en)", new ValueCallback() {
					@Override
					public void execute(final String value) {
						service.newConcept(parentDbId, value, new AsyncCallback<Long>() {
							public void onFailure(Throwable t) {
							}

							@Override
							public void onSuccess(Long aId) {
								TreeNode rec = new SkosConcept(new TransportSkosConcept(aId, value, false, false));
								rec.setAttribute("parentId", parentId);
								skosTree.addData(rec);
							}
						});
					}
				});
			}
		});

		final SelectItem action = new SelectItem("action", "Selected Concepts");
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>(3);
		valueMap.put("nothing", "Selected Concepts");
		valueMap.put("delete", "Remove");
		valueMap.put("same", "Set as owl:sameAs");
		valueMap.put("merge", "Merge");
		action.setValueMap(valueMap);
		action.setDefaultToFirstOption(true);
		action.setDisabled(true);
		action.setShowTitle(false);
		action.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				action.setValue("nothing");
				if(event.getValue().toString().equals("nothing"))
					return;
				if(event.getValue().toString().equals("same"))
				{
					List<Long> same = new ArrayList<Long>(8);
					for(Record r : skosTree.getSelection())
					{
						same.add(new Long(idCache.get(r.getAttributeAsInt("id")).getAttributeAsInt("dbId")));
					}
					service.setSame(same, emptyBoolCallback);
				}
				if(event.getValue().toString().equals("delete"))
				{
					SC.confirm("Are you sure you want to delete the <strong>"+skosTree.getSelection().length+"</strong> selected items?", new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							if(value == null || value != true)
								return;
							// parent -> child
							List<String> toDelete = new ArrayList<String>(8);
							for(Record r : skosTree.getSelection())
							{
								toDelete.add(
										new Long(idCache.get(r.getAttributeAsInt("parentId")).getAttributeAsInt("dbId"))+"-"+
										new Long(idCache.get(r.getAttributeAsInt("id")).getAttributeAsInt("dbId")));
								skosTree.removeData(r);
							}
							service.removeConcepts(toDelete, emptyBoolCallback);
							newConcept.setDisabled(true);
							action.setDisabled(true);
						}
					});
				}
				if(event.getValue().toString().equals("merge") && skosTree.getSelection().length > 1)
				{
					final Window w = new Window();
					w.setTitle("Merge Concepts");
					w.setIsModal(true);
					w.setShowMinimizeButton(false);
					w.centerInPage();
					w.setWidth(250);
					w.setHeight(100);
					w.addCloseClickHandler(new CloseClickHandler() {
						@Override
						public void onCloseClick(CloseClientEvent event) {
							w.destroy();
						}
					});
					
					DynamicForm form = new DynamicForm();
					form.setWidth100();
					form.setTitleOrientation(TitleOrientation.TOP);
					
					final SelectItem select = new SelectItem("Merge <strong>"+(skosTree.getSelection().length-1)+"</strong> Items into the following one");
					final LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>(3);
					for(Record r : skosTree.getSelection())
						valueMap.put(r.getAttribute("id"), r.getAttribute("label"));
					select.setValueMap(valueMap);
					select.setDefaultToFirstOption(true);
					select.setWidth(200);
					
					form.setFields(select);
					
					w.addItem(form);
					
					HLayout buttons = new HLayout();
					
					IButton merge = new IButton("Merge");
					merge.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							Record primaryRec = idCache.get(Integer.parseInt((String)select.getValue()));
							int primary = primaryRec.getAttributeAsInt("dbId");

							boolean hasChild = primaryRec.getAttributeAsRecordArray("children") != null && primaryRec.getAttributeAsRecordArray("children").length > 0;
							if(!hasChild)
								for(String s : valueMap.keySet())
								{
									Record r = idCache.get(Integer.parseInt(s));
									hasChild = r.getAttributeAsRecordArray("children") != null && r.getAttributeAsRecordArray("children").length > 0;
									if(hasChild)
										break;
								}

							List<Long> l = new ArrayList<Long>(8);
							List<Long> parents = new ArrayList<Long>(8);
							parents.add(new Long(primaryRec.getAttributeAsInt("parentId")));
							skosTree.removeData(primaryRec);
							Record newRec = new SkosConcept(new TransportSkosConcept(primaryRec.getAttributeAsInt("dbId"),
									primaryRec.getAttribute("label"), hasChild, false));
							newRec.setAttribute("parentId", primaryRec.getAttribute("parentId"));
							skosTree.addData(newRec);
							detailsCache.remove(primaryRec.getAttributeAsInt("dbId"));

							for(String s : valueMap.keySet())
							{
								Record r = idCache.get(Integer.parseInt(s));
								if(primary != r.getAttributeAsInt("dbId"))
								{
									l.add(new Long(r.getAttributeAsInt("dbId")));
									skosTree.removeData(r);
									if(!parents.contains(new Long(r.getAttributeAsInt("parentId"))))
									{
										detailsCache.remove(r.getAttributeAsInt("dbId"));
										parents.add(new Long(r.getAttributeAsInt("parentId")));
										newRec = new SkosConcept(new TransportSkosConcept(primaryRec.getAttributeAsInt("dbId"),
												primaryRec.getAttribute("label"), hasChild, false));
										newRec.setAttribute("parentId", r.getAttribute("parentId"));
										skosTree.addData(newRec);
									}
								}
							}
							service.mergeInto(primary, l, emptyBoolCallback);
							w.destroy();
						}
					});
					
					IButton cancel = new IButton("Cancel");
					cancel.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							w.destroy();
						}
					});
					
					buttons.setMembers(new LayoutSpacer(), merge, cancel);
					
					w.addItem(buttons);
					
					w.show();
				}
			}
		});

		skosTree.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				newConcept.setDisabled(skosTree.getSelectedRecord() == null);
				action.setDisabled(skosTree.getSelectedRecord() == null);
			}
		});
		
		VLayout middle = new VLayout();
		HLayout middleButtons = new HLayout();
		middleButtons.setAutoHeight();
		
		IButton newScheme = new IButton("New ConceptScheme");
		newScheme.setAutoFit(true);
		newScheme.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SC.askforValue("ConceptScheme Title", new ValueCallback() {
					@Override
					public void execute(final String value) {
						service.newConceptScheme(value, new AsyncCallback<Long>() {
							public void onFailure(Throwable t) {
							}

							@Override
							public void onSuccess(Long aId) {
								TreeNode rec = new SkosConcept(new TransportSkosConcept(aId, value, false, true));
								rec.setAttribute("parentId", 0);
								skosTree.addData(rec);
							}
						});
					}
				});
			}
		});
		
		DynamicForm f = new DynamicForm();
		f.setFields(action);
		middleButtons.setMembers(newScheme, new LayoutSpacer(), newConcept, new LayoutSpacer(), f);
		
		middle.setMembers(middleButtons, skosTree);
		
		freeGrid = new ListGrid();
		service.getFreeTags(new AsyncCallback<List<TransportTag>>() {
			public void onFailure(Throwable t) {
			}

			@Override
			public void onSuccess(List<TransportTag> result) {
				Record[] rs = new Record[result.size()];
				for(int i = 0; i < result.size(); i++)
					rs[i] = new FreeTag(result.get(i));
				freeGrid.setData(rs);
			}
		});
		freeGrid.setFields(new TreeGridField("tag", "Tags"));
		freeGrid.setCanDragRecordsOut(true);
		
		leftPane = new VLayout();
		Label l = new Label("Double-click a concept to edit its details.");
		l.setAutoHeight();
		l.setPadding(5);
		leftPane.setMembers(l);
		
		VLayout rightPane = new VLayout();
		Label l2 = new Label("Drag these tags to the SKOS Tree to categorize them.");
		l2.setAutoHeight();
		l2.setPadding(5);
		rightPane.setMembers(l2, freeGrid);

		HLayout pageLayout = new HLayout();
		pageLayout.setHeight100();
		pageLayout.setWidth100();
		pageLayout.setMembers(leftPane, middle, rightPane);
		RootPanel.get("gwtCanvas").add(pageLayout);
		//output = new Canvas();
		//RootPanel.get("gwtCanvas").add(output);
	}
}
