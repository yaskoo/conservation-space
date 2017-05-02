package com.sirma.itt.emf.semantic.patch;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sirma.itt.seip.db.DatabaseConfiguration;
import com.sirma.itt.seip.db.patch.DatabasePatcher;
import com.sirma.itt.seip.exception.RollbackedException;
import com.sirma.itt.seip.plugin.ExtensionPoint;
import com.sirma.itt.seip.runtime.boot.Startup;
import com.sirma.itt.seip.runtime.boot.StartupPhase;
import com.sirma.itt.seip.security.annotation.RunAsAllTenantAdmins;
import com.sirma.itt.seip.time.TimeTracker;

/**
 * Patch service that initialize semantic database or upgrade it if needed.
 * <p>
 * <b>NOTE:</b> The application have to ensure that this class is executed before any other have accessed the database.
 * <p>
 *
 * @author BBonev
 */
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SemanticPatchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private DatabaseConfiguration databaseConfiguration;

	@Inject
	@ExtensionPoint(SemanticSchemaPatches.NAME)
	private Iterable<SemanticSchemaPatches> schemaPatches;

	/**
	 * Executes the patching algorithm using the liquibase library.
	 *
	 * @throws RollbackedException
	 *             the rollbacked exception
	 */
	@RunAsAllTenantAdmins
	@Startup(phase = StartupPhase.BEFORE_APP_START, order = 5.4, async = false)
	public void patchSchemaAllTenants() throws RollbackedException {
		runPatches();
	}

	/**
	 * Run patches for the current tenant.
	 *
	 * @throws RollbackedException
	 *             the rollbacked exception
	 */
	public void runPatches() throws RollbackedException {
		LOGGER.info("Starting semantic database patch system...");
		TimeTracker tracker = TimeTracker.createAndStart();

		DatabasePatcher.patchDatabase(databaseConfiguration.getDataSource(), schemaPatches);

		LOGGER.info("Database update complete in " + tracker.stop() + " ms");
	}

	/**
	 * Run custom patches for the current tenant.
	 *
	 * @param patches
	 *            are the custom patches
	 * @throws RollbackedException
	 *             the rollbacked exception
	 */
	public void runPatches(Collection<SemanticSchemaPatches> patches) throws RollbackedException {
		if (patches == null || patches.isEmpty()) {
			return;
		}
		LOGGER.info("Starting semantic runtime database patch system...");
		TimeTracker tracker = TimeTracker.createAndStart();

		DatabasePatcher.patchDatabase(databaseConfiguration.getDataSource(), patches);

		LOGGER.info("Database runtime update complete in " + tracker.stop() + " ms");
	}
}