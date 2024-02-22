import org.junit.*;
import java.util.logging.Logger;
import gash.payload.BasicBuilder;
import gash.payload.Message;

public class BuilderTest {
	static final String N = "fred";
	static final String G = "dogs";
	static final String T = "hello";
	static final String MSG = "dogs,fred,hello";
	private Logger logger;

	@Test
	public void testBuilder() {
		BasicBuilder builder = new BasicBuilder();

		String s = builder.encode(new Message(N,G,T));
		logger.info("msg: " + s);

	}
}
