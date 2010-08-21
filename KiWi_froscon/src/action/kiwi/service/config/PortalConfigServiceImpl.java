package kiwi.service.config;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import kiwi.api.config.ConfigurationService;
import kiwi.api.config.PortalConfigService;
import kiwi.api.config.PortalConfigServiceLocal;
import kiwi.api.config.PortalConfigServiceRemote;
import kiwi.api.extension.ExtensionService;
import kiwi.api.extension.KiWiApplication;
import kiwi.exception.KiWiException;
import kiwi.model.user.User;
import kiwi.transport.client.WidgetConfig;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Stateless
@Name("kiwi.core.portalConfigService")
@Scope(ScopeType.STATELESS)
public class PortalConfigServiceImpl implements PortalConfigServiceLocal, PortalConfigServiceRemote {

//    @Logger
//    private Log log;

	@In
	private ExtensionService extensionService;

	/**
	 * @see PortalConfigService#getNumberOfPortalColumns(User, String)
	 */
	@Override
	public int getNumberOfPortalColumns(User user, String applicationId) {
		List<WidgetConfig> widgetConfigList = getPortalConfiguration(user, this.buildConfigKey(applicationId));
		if(widgetConfigList==null) return 0;
		int maxColumn=0;
		for(WidgetConfig widgetConfig:widgetConfigList){
			maxColumn = (maxColumn>widgetConfig.getColumn()) ? maxColumn : widgetConfig.getColumn(); 
		}
		return maxColumn+1;
	}

	/**
	 * @see PortalConfigService#getPortalConfiguration(User, String)
	 */
	@Override
	public List<WidgetConfig> getPortalConfiguration(User user, String applicationId) {
		ConfigurationService configurationService = (ConfigurationService)Component.getInstance("configurationService");

		List<String> stringList = configurationService.getUserConfiguration(user, this.buildConfigKey(applicationId)).getListValue();
		if(stringList == null || stringList.size() == 0){
			KiWiApplication kiwiApplication = extensionService.getApplication(applicationId);
			if(kiwiApplication == null){
				return new LinkedList<WidgetConfig>();
			} else{
				List<WidgetConfig> portalConfig = kiwiApplication.getDefaultPortalConfig();
				// sort
				portalConfig = sortWidgetConfigList(portalConfig);
				
				return portalConfig;
			}
		}
		return stringListToWidgetConfigurationList(stringList);
	}

	/**
	 * @see PortalConfigService#setPortalConfiguration(User, String, List)
	 */
	@Override
	public void setPortalConfiguration(User user, String applicationId, List<WidgetConfig> properties) {
		ConfigurationService configurationService = 
			(ConfigurationService)Component.getInstance("configurationService");
		if(properties == null){
			configurationService.removeUserConfiguration(user, buildConfigKey(applicationId));
		} else {
			// sort
			properties = sortWidgetConfigList(properties);
			configurationService.setUserConfiguration(
					user, 
					buildConfigKey(applicationId), 
					widgetConfigListToUserStringList(properties));
		}
	}

	/**
	 * @see PortalConfigService#setWidgetConfiguration(User, String, WidgetConfig)
	 */
	@Override
	public void setWidgetConfiguration(User user, String applicationId,
			WidgetConfig config) throws KiWiException {
		List<WidgetConfig> widgetConfigs = getPortalConfiguration(user, applicationId);
		if(widgetConfigs == null) throw new KiWiException(
				"No default portalConfiguration for the Application " + applicationId + " found.");
		WidgetConfig widgetConfig = null;
		for(WidgetConfig wconf:widgetConfigs){
			if(config.getColumn() == wconf.getColumn() && 
				config.getRow() == wconf.getRow() &&
				config.getWidgetId() == wconf.getWidgetId())
				widgetConfig = wconf;
		}
		if(widgetConfig.equals(null)) 
			throw new KiWiException("Error saving widget configuration: widgets not synchron. Please reload the page and try again.");
		setPortalConfiguration(user, applicationId, widgetConfigs);
	}

	/**
	 * @see kiwi.api.config.PortalConfigService#resetPortalConfiguration(User, String)
	 */
	@Override
	public void resetPortalConfiguration(User user, String applicationId) {
		setPortalConfiguration(user, applicationId, null);
	}


	/**
	 * Makes a list of Strings out of a list of a list of WidgetConfigs
	 * @param properties
	 * @return
	 */
	private List<String> widgetConfigListToUserStringList(List<WidgetConfig> properties) {
		List<String> res = new LinkedList<String>();
		for(WidgetConfig widgetConfig:properties){
			res.addAll(widgetConfig.serialize());
		}
		return res;
	}

	private List<WidgetConfig> stringListToWidgetConfigurationList(List<String> userConfigList) {
		if(userConfigList == null) return null;
		List<WidgetConfig> res = new LinkedList<WidgetConfig>();
		while(userConfigList.size()>0){
			WidgetConfig wc = new WidgetConfig();
			wc.deserialize(userConfigList);
			res.add(wc);
		}
		return res;
	}

	private String buildConfigKey(String applicationId) {
		String res = "PortalCfg_" + applicationId;
		return res;
	}

	private List<WidgetConfig> sortWidgetConfigList(
			List<WidgetConfig> widgetConfigList) {
		Comparator<WidgetConfig> comparator = new Comparator<WidgetConfig>() {
			@Override
			public int compare(WidgetConfig arg0, WidgetConfig arg1) {
				if(arg0.getColumn()<arg1.getColumn())return 0;
				if(arg0.getColumn()>arg1.getColumn())return 1;
				if(arg0.getRow()<arg1.getRow())return 0;
				if(arg0.getRow()>arg1.getRow())return 1;
				return 0;
			}
		};
		WidgetConfig[] array = widgetConfigList.toArray(new WidgetConfig[]{});
		Arrays.sort(array, comparator);
		return Arrays.asList(array);
	}



}
