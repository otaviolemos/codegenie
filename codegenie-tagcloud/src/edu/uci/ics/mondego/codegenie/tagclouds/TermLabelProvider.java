package edu.uci.ics.mondego.codegenie.tagclouds;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.cloudio.ICloudLabelProvider;

public class TermLabelProvider extends BaseLabelProvider implements
		ICloudLabelProvider {


    private Font font;
    private double maxWeight;


    public TermLabelProvider(Font font,double maxWeight) {
        this.font = font;
        this.maxWeight = maxWeight;
    }

    public String getLabel(Object element) {
    	Term term = (Term)element;
        return term.getTerm();
    }


    public double getWeight(Object element) {
    	Term term = (Term)element;
        return term.getWeight()/maxWeight;
    }


    public Color getColor(Object element) {
    	Term term = (Term)element;
    	if(term.getHits()>70)
        	return new Color(Display.getDefault(), 0 ,255, 0);
    	else if(term.getHits()>60)
    		return new Color(Display.getDefault(), 0, 205, 0);
    	else if(term.getHits()>50)
    		return new Color(Display.getDefault(), 102 ,205, 0);
    	else if(term.getHits()>40)
    		return new Color(Display.getDefault(), 154 ,205, 50);
    	else if(term.getHits()>30)
    		return new Color(Display.getDefault(), 179, 231, 167);
    	else if(term.getHits()>10)
    		return new Color(Display.getDefault(), 202, 255 ,112);
    	else //if(term.getHits()>0)
    		return new Color(Display.getDefault(), 255 ,255, 255);
    }
    

    public FontData[] getFontData(Object element) {
        return font.getFontData();
    }


    public float getAngle(Object element) {
    	return 0;
//        return (float) (-90 + Math.random() * 180);
    }

    public String getToolTip(Object element) {
    	Term term = (Term)element;
        return "W: " + term.getWeight() +"/Hits: "+term.getHits();
    }

}
