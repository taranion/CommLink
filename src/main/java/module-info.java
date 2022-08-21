open module de.rpgframework.shadowrun6.commlink6 {
	exports de.rpgframework.shadowrun6.comlink;
	exports de.rpgframework.shadowrun6.comlink.pages;
	
	provides java.lang.System.LoggerFinder with de.rpgframework.shadowrun6.comlink.CustomLoggerFinder;

	requires com.gluonhq.attach.browser;
	requires com.gluonhq.attach.util;
	requires de.rpgframework.core;
	requires de.rpgframework.eden.client;
	requires de.rpgframework.eden.client.jfx;
	requires de.rpgframework.javafx;
	requires de.rpgframework.rules;
	requires de.rpgframework.shadowrun6.chargen.jfx;
	requires de.rpgframework.shadowrun6.core;
	requires de.rpgframework.shadowrun6.data;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.extensions;
	requires javafx.fxml;
	requires javafx.graphics;
	requires shadowrun.common;
	requires shadowrun.common.chargen.jfx;
	requires java.desktop;
}