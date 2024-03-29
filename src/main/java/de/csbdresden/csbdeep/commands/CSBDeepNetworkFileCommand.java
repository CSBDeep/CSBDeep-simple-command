/*-
 * #%L
 * CSBDeep: CNNs for image restoration of fluorescence microscopy.
 * %%
 * Copyright (C) 2017 - 2018 Deborah Schmidt, Florian Jug, Benjamin Wilhelm
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package de.csbdresden.csbdeep.commands;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.command.CommandModule;
import org.scijava.command.CommandService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 */
@Plugin(type = Command.class,
	menuPath = "Plugins>CSBDeep>Demo>3D Denoising - Planaria", headless = true)
public class CSBDeepNetworkFileCommand implements Command {

	@Parameter(type = ItemIO.INPUT)
	public Dataset input;

	@Parameter(type = ItemIO.OUTPUT)
	protected Dataset output;

	@Parameter(label = "Number of tiles", min = "1")
	protected int nTiles = 8;

	@Parameter(label="Show progress dialog")
	protected boolean showProgressDialog = true;

	@Parameter
	CommandService commandService;

	@Override
	public void run() {

		String modelFile = getClass().getResource("denoise3D/model.zip").getFile();

		try {
			final CommandModule module = commandService.run(
					GenericNetwork.class, false,
					"input", input,
					"modelFile", modelFile,
	//				"batchSize", 10,
	//				"batchAxis", Axes.TIME.getLabel(),
					"blockMultiple", 8,
					"nTiles", nTiles,
					"showProgressDialog", showProgressDialog).get();
			output = (Dataset) module.getOutput("output");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}

	public static void main(final String... args) throws Exception {
		// create the ImageJ application context with all available services
		final ImageJ ij = new ImageJ();

		ij.launch(args);

		// load the dataset
		final Dataset dataset = ij.scifio().datasetIO().open("http://samples.fiji.sc/blobs.png");

		// show the image
		ij.ui().show(dataset);

		// invoke the plugin
		ij.command().run(CSBDeepNetworkFileCommand.class, true);

	}
}
