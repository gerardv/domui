package to.etc.domui.util.db;

import to.etc.webapp.query.*;

public interface IModelCopier {
	<T> T copyInstanceShallow(QDataContext dc, T source) throws Exception;

	<T> T copyInstanceDeep(QDataContext targetdc, QDataContext sourcedc, T source) throws Exception;
}