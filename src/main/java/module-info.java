open module de.rpgframework.commlink6binary {
	exports de.rpgframework.shadowrun6.commlinkbinary;

	requires de.rpgframework.core;
	requires de.rpgframework.eden.client;
	requires de.rpgframework.eden.client.jfx;
	requires de.rpgframework.javafx;
	requires de.rpgframework.rules;
	requires de.rpgframework.shadowrun6.chargen.jfx;
	requires de.rpgframework.shadowrun6.core;
	requires de.rpgframework.shadowrun6.data;
	requires de.rpgframework.shadowrun6.export.fvtt;
	requires de.rpgframework.shadowrun6.export.json;
	requires shadowrun6.export.pdf.standard;

	requires javafx.base;
	requires javafx.controls;
	requires javafx.extensions;
	requires javafx.graphics;
	requires shadowrun.common;
	requires shadowrun.common.chargen.jfx;
	requires java.desktop;
//	requires com.gluonhq.attach.browser;
	requires com.gluonhq.attach.util;
//	requires com.gluonhq.attach.device;
	requires java.logging;
	requires rpgframework.pdfviewer;
	requires java.prefs;
	requires org.controlsfx.controls;
	requires de.rpgframework.shadowrun6.commlink6;

}