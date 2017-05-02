import {Injectable, Inject, Event} from 'app/app';
import {ConfigurationRestService} from 'services/rest/configurations-service';
import {Eventbus} from 'services/eventbus/eventbus';
import {CommandChain} from 'common/command-chain/command-chain';
import {BooleanConverter, NumberConverter, StringConverter} from 'common/convert/type-converter';

/**
 * Service responsible for loading application configurations.
 *
 * The service listens for {@link ConfigurationsUpdateEvent} allowing remapping of existing configurations with
 * the provided in the event.
 *
 * @author svelikov
 */
@Injectable()
@Inject(ConfigurationRestService, Eventbus)
export class Configuration {

  constructor(configurationRestService, eventbus) {
    this.configurationRestService = configurationRestService;
    this.eventbus = eventbus;
    this.configs = {};
    this.converter = new CommandChain([new BooleanConverter(), new NumberConverter(), new StringConverter()]);
    this.registerConfigurationUpdateListener();
  }

  /**
   * Registers a handler that reloads the configurations when {@link ConfigurationsUpdateEvent} is fired avoiding having
   * to refresh the application to load the updated configurations.
   */
  registerConfigurationUpdateListener() {
    this.eventbus.subscribe(ConfigurationsUpdateEvent, (updatedConfigurations) => {
      this.populateConfigurations(updatedConfigurations[0]);
    });
  }

  load() {
    return this.configurationRestService.loadConfigurations().then((response) => {
      this.populateConfigurations(response.data);
      this.eventbus.publish(new ConfigurationsLoadedEvent(response.data));
      return this.configs;
    });
  }

  populateConfigurations(configurations) {
    this.toMap(configurations, this.configs);

    // TODO: development config is hardcodded but should be taken from elsewhere!
    this.configs[Configuration.APPLICATION_MODE_DEVELOPMENT] = true;
    this.configs[Configuration.UI_TEXTAREA_MIN_CHARS] = 50;
    this.configs[Configuration.IDOC_TABS_TITLE_MAX_LENGTH] = 50;
  }

  /**
   * Configurations are returned as an array, so we need to convert them to a map for easier access.
   *
   * @param source - the array of configurations to be mapped
   * @param dest - map to be populated with the configuration key as key and the configuration itself as value
   */
  toMap(source, dest) {
    for (let i = 0; i < source.length; i++) {
      dest[source[i].key] = source[i].value;
    }
  }

  /**
   * Get configuration property value.
   *
   * @param key The property name/key
   * @returns The property value.
   */
  get(key) {
    if (this.configs[key] !== undefined) {
      return this.converter.execute(this.configs[key]);
    }
  }

  /**
   * Splits a configuration property value into multiple values based on a provided separator and returns them as an
   * array.
   *
   * @param key - the property's key
   * @param separator - the separator used for splitting the value. Default if not provided is the comma character
   * @returns array of values or an empty array if there is no property with the provided key
   */
  getArray(key, separator = ',') {
    var values = this.get(key);
    if (values) {
      return values.split(separator).map((value) => {
        return value.trim();
      });
    }
    return [];
  }
}

/**
 * Event carrying array of configurations that are updated somehow.
 *
 * Designed for components that need to be notified when the configurations are changed.
 *
 * @author Mihail Radkov
 */
@Event()
export class ConfigurationsUpdateEvent {
  constructor() {
    this.args = arguments;
  }

  getData() {
    return this.args;
  }
}

@Event()
export class ConfigurationsLoadedEvent {
  constructor() {
    this.args = arguments;
  }

  getData() {
    return this.args;
  }
}

Configuration.APPLICATION_MODE_DEVELOPMENT = 'application.mode.development';
Configuration.UI_TEXTAREA_MIN_CHARS = 'ui.textarea.min.chars';
Configuration.IDOC_TABS_TITLE_MAX_LENGTH = 'idoc.tabs.title.max.length';
Configuration.UI_DATE_FORMAT = 'ui.date.format';
Configuration.UI_TIME_FORMAT = 'ui.time.format';
Configuration.UPLOAD_MAX_FILE_SIZE = 'file.upload.maxsize';
Configuration.LOGO_LOCATION = 'application.logo.image.path';
Configuration.RNC_DEBUG_ENABLED = 'clientside.rnc.debug.mode';
Configuration.SESSION_TIMEOUT = 'session.timeout.period';
Configuration.SESSION_TIMEOUT_REDIRECT = 'session.timeout.redirect';
Configuration.USER_RECENT_OBJECTS_SIZE = 'user.recent.objects.size';
Configuration.DRAFT_AUTOSAVE_INTERVAL = 'idoc.draftAutosaveIntervalMillis';
Configuration.SEARCH_PAGE_SIZE = 'search.result.pager.pagesize';
Configuration.SEARCH_PAGER_MAX_PAGES = 'search.result.pager.maxpages';
Configuration.HELP_SUPPORT_CODELIST_MAIL_TYPE = 'help.support.codelist.mail.type';