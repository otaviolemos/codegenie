package br.unifesp.ict.seg.codegenie.views;

import org.eclipse.core.runtime.NullProgressMonitor;

public class GRProgressMonitor extends NullProgressMonitor {
	@Override
	public void setCanceled(boolean cancelled) {
		isDone = true;  // bail early
		super.setCanceled(cancelled);
	}

	private boolean isDone = false;
	public synchronized boolean isDone() {
		return isDone;
	}

	@Override
	public void done() {
		isDone = true;
		super.done();
	}
	
	public static void waitMonitor(GRProgressMonitor m){
		while(!m.isDone()){
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
