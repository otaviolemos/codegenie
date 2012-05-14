/**
 * @author ricardo
 */

package edu.uci.ics.mondego.codegenie.search.results;

public class Entity {

	public static final class TypeEnum
	{
		public static final int PACKAGE = 1;

		public static final int CLASS = 2;

		public static final int INTERFACE = 3;

 		public static final int METHOD = 4;

		public static final int FIELD = 5;

		public static final int CONSTRUCTOR = 6;

		public static final int STATIC_INIT = 7;

		public static int[] typeArray = {PACKAGE, CLASS, INTERFACE, METHOD, FIELD, CONSTRUCTOR, STATIC_INIT};
	
		public static final String getName(int type) {
			switch (type) {
			case PACKAGE:
				return "PACKAGE";

			case CLASS:
				return "CLASS";

			case INTERFACE:
				return "INTERFACE";

	 		case METHOD:
				return "METHOD";

			case FIELD:
				return "FIELD";

			case CONSTRUCTOR:
				return "CONSTRUCTOR";

			case STATIC_INIT:
				return "STATIC_INIT";

			default:
				throw new RuntimeException("BUG!!!!");
			}
		}
	}

}
