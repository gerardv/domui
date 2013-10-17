package to.etc.webapp.json;

import java.io.*;

import javax.annotation.*;

import to.etc.lexer.*;

public class JsonReader extends ReaderTokenizerBase {
	@Nonnull
	final private JsonTypeRegistry m_registry;

	public JsonReader(Object source, Reader r, @Nonnull JsonTypeRegistry registry) {
		super(source, r);
		m_registry = registry;
	}

	@Nullable
	public <T> T parse(@Nonnull Class<T> typeClass) throws Exception {
		ITypeMapping mapping = m_registry.createMapping(typeClass, null);
		if(null == mapping)
			throw new IllegalStateException("Could not find a json mapping for " + typeClass);
		return (T) mapping.parse(this);
	}

}
