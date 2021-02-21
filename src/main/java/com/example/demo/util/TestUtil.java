package com.example.demo.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TestUtil {

	public TestUtil(OkHeaderSet okHeaderNames, NgHeaderSet ngHeaderNames) {

		if(Objects.isNull(okHeaderNames)) {
			okHeaderNames = new OkHeaderSet();
		}
		if(Objects.isNull(ngHeaderNames)) {
			ngHeaderNames = new NgHeaderSet();
		}

		this.okHeaderNames = okHeaderNames;
		this.ngHeaderNames = ngHeaderNames;
	}

	private final OkHeaderSet okHeaderNames;

	private final NgHeaderSet ngHeaderNames;

	public void doSay() {
		System.out.println("test dayo");
	}

	public boolean isOkHeader(String headerName) {

		return okHeaderNames.contains(headerName);

	}

	public boolean isNgHeader(String headerName) {

		return ngHeaderNames.contains(headerName);

	}

	public static class HeaderSet{
		private final Set<String> hederNames;

		public HeaderSet(String... headerNames) {
			hederNames = new HashSet<String>();
			for(String headerName : headerNames) {
				hederNames.add(headerName.toLowerCase());
			}
		}

		public boolean contains(String headerName) {
			return hederNames.contains(headerName.toLowerCase());
		}
	}

	public static class OkHeaderSet extends HeaderSet{
		public OkHeaderSet(String... headerNames) {
			super(headerNames);
		}
	}

	public static class NgHeaderSet extends HeaderSet{
		public NgHeaderSet(String... headerNames) {
			super(headerNames);
		}
	}

}
