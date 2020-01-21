package to.etc.domui.util.importers;

import org.eclipse.jdt.annotation.Nullable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 31-10-17.
 */
public interface IImportColumn {
	@Nullable String getName();

	@Nullable String getStringValue();

	@Nullable Date asDate();

	@Nullable Long asLong();

	@Nullable Integer asInteger();

	@Nullable BigDecimal getDecimal();

	@Nullable
	default String getStringValueStripped() {
		String v = getStringValue();
		if(null == v)
			return v;

		v = v.replaceAll("(^\\h*)|(\\h*$)","");	// Replace all spaces, even nbsp's and others, sigh.
		v = v.replaceAll("(\\h+)"," ");	// Replace all inner spaces with " "
		return v.length() == 0 ? null : v;
	}
}
