/**
 *
 */
package com.sirma.itt.seip.instance.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.sirma.itt.seip.content.Content;
import com.sirma.itt.seip.content.ContentStore;
import com.sirma.itt.seip.content.StoreItemInfo;
import com.sirma.itt.seip.domain.instance.EmfInstance;
import com.sirma.itt.seip.instance.integration.InstanceDispatcher;
import com.sirma.itt.seip.testutil.mocks.InstanceProxyMock;

/**
 * Tests for {@link ContentStoreProviderImpl}.
 * 
 * @author BBonev
 */
public class ContentStoreProviderImplTest {

	@InjectMocks
	private ContentStoreProviderImpl provider;

	@Mock
	private ContentStore localStore;

	@Mock
	private ContentStore tempStore;

	@Spy
	private InstanceProxyMock<ContentStore> stores = new InstanceProxyMock<>(null);

	@Mock
	private ContentStore mockStore;

	@Mock
	private InstanceDispatcher instanceDispatcher;

	private Content content = Content.createEmpty();

	@Before
	public void beforeMethod() {
		MockitoAnnotations.initMocks(this);
		// reset the collection before each test method
		stores.set(mockStore);
		stores.add(localStore);
		stores.add(tempStore);
		when(localStore.getName()).thenReturn("local");
		when(tempStore.getName()).thenReturn("temp");
		when(mockStore.getName()).thenReturn("any");

		provider.init();
	}

	@Test
	public void getStoreByName() throws Exception {
		assertNotNull(provider.getStore("local"));
		assertEquals(localStore, provider.getStore("local"));

		assertNotNull(provider.getStore("temp"));
		assertEquals(tempStore, provider.getStore("temp"));

		assertNotNull(provider.getStore("any"));
		assertEquals(mockStore, provider.getStore("any"));

		assertEquals(localStore, provider.getStore("someOther"));
	}

	@Test
	public void getStoreByInstance() throws Exception {
		EmfInstance instance = new EmfInstance();
		instance.setId("emf:instance");

		assertEquals(localStore, provider.getStore(instance, content));

		when(instanceDispatcher.getContentManagementSystem(instance, content)).thenReturn("any");

		assertEquals(mockStore, provider.getStore(instance, content));
	}

	@Test
	public void getViewStoreByInstance() throws Exception {
		EmfInstance instance = new EmfInstance();
		instance.setId("emf:instance");

		assertEquals(localStore, provider.getViewStore(instance, content));

		when(instanceDispatcher.getViewManagementSystem(instance, content)).thenReturn("any");

		assertEquals(mockStore, provider.getViewStore(instance, content));
	}

	@Test
	public void getLocalStore() throws Exception {
		assertNotNull(provider.getLocalStore());
	}

	@Test
	public void getTempStore() throws Exception {
		assertNotNull(provider.getTempStore());
	}

	@Test
	public void getStoreByStoreInfo() throws Exception {
		assertNull(provider.getStore((StoreItemInfo) null));
		assertNull(provider.getStore(new StoreItemInfo()));
		assertEquals(mockStore, provider.getStore(new StoreItemInfo().setProviderType("any")));
		assertEquals(localStore, provider.getStore(new StoreItemInfo().setProviderType("someOther")));
	}

}