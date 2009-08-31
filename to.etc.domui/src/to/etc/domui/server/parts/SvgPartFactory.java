package to.etc.domui.server.parts;

import java.io.*;

import org.apache.batik.transcoder.*;
import org.apache.batik.transcoder.image.*;

import to.etc.domui.server.*;
import to.etc.domui.util.resources.*;

/**
 * Themed SVG generator.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Aug 31, 2009
 */
public class SvgPartFactory implements IBufferedPartFactory, IUrlPart {
	public boolean accepts(String rurl) {
		return rurl.endsWith(".svg.png");
	}
	public Object decodeKey(String rurl, IParameterInfo param) throws Exception {
		return rurl;
	}

	public void generate(PartResponse pr, DomApplication da, Object key, ResourceDependencyList rdl) throws Exception {
		//-- 1. Get the input as a theme-replaced resource
		String svg = da.getThemeReplacedString(rdl, (String) key);

		//-- 2. Now generate the thingy using the Batik transcoder:
		PNGTranscoder coder = new PNGTranscoder();
		//		coder.addTranscodingHint(PNGTranscoder., null);
		TranscoderInput in = new TranscoderInput(new StringReader(svg));
		TranscoderOutput out = new TranscoderOutput(pr.getOutputStream());
		coder.transcode(in, out);

		pr.setMime("image/png");
	}
}
