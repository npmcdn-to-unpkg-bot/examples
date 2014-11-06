package com.slavi.util.web;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class ContextUtil {
	static void dumpContext(Context ctx, String prefix, StringBuilder result) throws NamingException {
		NamingEnumeration<NameClassPair> lst = ctx.list("");
		while (lst.hasMore()) {
			NameClassPair pair = lst.next();
			Object o = ctx.lookup(pair.getName());
			if (Context.class.isAssignableFrom(o.getClass())) {
				dumpContext((Context) o, prefix + "/" + pair.getName() + "/", result);
			} else {
				result.append(prefix);
				result.append(pair.getName());
				result.append(" <");
				result.append(pair.getClassName());
				result.append(">");
				result.append("\n");
			}
		}
	}

	static void dumpContext_OLD(Context context, String ident, StringBuilder result) throws NamingException {
		NamingEnumeration<Binding> list = context.listBindings("");
		while (list.hasMore()) {
			Binding binding = list.next();
			result.append(ident);
			result.append(binding.getName());
			Object object = binding.getObject();
			if (object instanceof Context) {
				Context c = (Context) object;
				result.append("\n");
				dumpContext(c, ident + "    ", result);
			} else {
				result.append(":");
				result.append(binding.getClassName());
				result.append("\n");
			}
		}
	}

	public static String contextToString(Context context) throws NamingException {
		StringBuilder result = new StringBuilder("CONTEXT:\n");
		dumpContext(context, "", result);
		return result.toString();
	}
}
