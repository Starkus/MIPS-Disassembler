package net.starkus.mipsstudio.control;

import java.util.List;
import java.util.function.Function;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableView;

public class LazyLoadTableView<S> extends TableView<S> {
	
	private Function<List<S>, List<S>> loadMoreFunction;
	
	public void setUpScrollBarListener(ScrollBar scrollBar)
	{
		scrollBar.valueProperty().addListener((obs, oldv, newv) -> {
			// Debug
			//System.out.println(newv.doubleValue());

			// If scrolled to bottom
			if (newv.doubleValue() == scrollBar.getMax())
			{
				int oldSize = getItems().size();
				
				getItems().addAll(loadMoreFunction.apply(getItems()));
					
				double newSize = getItems().size();
				double added = newSize - oldSize;
				double x = added / newSize;
					
				scrollBar.setValue(1.0 - x);
			}
		});
	}
	
	public ScrollBar getScrollBar()
	{
		for (Node node : lookupAll(".scroll-bar"))
		{
			if (node instanceof ScrollBar)
			{
				ScrollBar bar = (ScrollBar) node;
				if (bar.getOrientation().equals(Orientation.VERTICAL))
					return bar;
			}
		}
		
		return null;
	}
	
	public void setLoadMoreFunction(Function<List<S>, List<S>> newFunc)
	{
		loadMoreFunction = newFunc;
	}
}
