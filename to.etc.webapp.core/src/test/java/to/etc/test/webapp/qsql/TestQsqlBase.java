package to.etc.test.webapp.qsql;

import javax.annotation.*;
import javax.sql.*;

import org.junit.*;

import org.junit.experimental.categories.Category;
import to.etc.puzzler.GroupUsesDatabase;
import to.etc.webapp.qsql.*;
import to.etc.webapp.query.*;
import to.etc.webapp.testsupport.*;

@Category(GroupUsesDatabase.class)
public class TestQsqlBase {
	@Nullable
	private QDataContext m_dc;

	@Before
	public void setUp() throws Exception {
		DataSource ds = TUtilTestProperties.getRawDataSource();

		m_dc = new JdbcDataContext(null, ds.getConnection());
	}

	@After
	public void tearDown() throws Exception {
		getDc().close();
	}

	@Nonnull
	protected QDataContext getDc() {
		QDataContext dc = m_dc;
		if(dc == null) {
			Assume.assumeFalse("Database is not present", true);
			//throw new IllegalStateException("m_dc not initialized yet!");
		}
		return dc;
	}

}
