package com.sirma.sep.email.patch;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sirma.email.ZimbraEmailIntegrationConstants;
import com.sirma.itt.seip.configuration.ConfigurationProperty;
import com.sirma.itt.seip.db.DbDao;
import com.sirma.itt.seip.testutil.mocks.ConfigurationPropertyMock;
import com.sirma.sep.email.configuration.EmailIntegrationConfiguration;
import com.sirma.sep.email.exception.EmailIntegrationException;
import com.sirma.sep.email.model.domain.ClassOfServiceInformation;
import com.sirma.sep.email.service.DomainAdministrationService;

import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;

/**
 * Test for {@link DisableGalAccessPatch}.
 *
 * @author S.Djulgerova
 */
public class DisableGalAccessPatchTest {

	@InjectMocks
	private DisableGalAccessPatch disableGalAccessPatch;

	@Mock
	private DomainAdministrationService domainAdministrationService;

	@Mock
	private EmailIntegrationConfiguration emailIntegrationConfiguration;

	@Mock
	private DbDao dbDao;

	@Before
	public void setUp() throws SetupException {
		disableGalAccessPatch = new DisableGalAccessPatch();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExecute_COSUpdate() throws CustomChangeException, EmailIntegrationException {
		List<String> mailboxSupportable = new ArrayList<>(1);
		mailboxSupportable.add("User");
		when(dbDao.fetchWithNamed(anyString(), anyList())).thenReturn(mailboxSupportable);
		mockCOS("sep");

		ClassOfServiceInformation cos = Mockito.mock(ClassOfServiceInformation.class);
		when(cos.getCosId()).thenReturn("123456");
		when(domainAdministrationService.getCosByName("sep")).thenReturn(cos);

		disableGalAccessPatch.execute(null);
		verify(domainAdministrationService, times(1)).modifyCos("123456",
				ZimbraEmailIntegrationConstants.ENABLE_GAL_FEATURE, "FALSE");
		verify(domainAdministrationService, times(1)).modifyCos("123456",
				ZimbraEmailIntegrationConstants.ENABLE_GAL_SEARCH, "FALSE");
	}

	@Test
	public void testExecute_NoMailboxSupportable() throws CustomChangeException, EmailIntegrationException {
		when(dbDao.fetchWithNamed(anyString(), anyList())).thenReturn(Collections.emptyList());
		mockCOS("sep");
		disableGalAccessPatch.execute(null);
		verify(domainAdministrationService, times(0)).modifyCos(anyString(), anyString(), anyString());
	}

	private void mockCOS(String cosName) {
		ConfigurationProperty<String> cos = new ConfigurationPropertyMock<>(cosName);
		when(emailIntegrationConfiguration.getTenantClassOfService()).thenReturn(cos);
	}

}