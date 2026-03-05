package com.QuestSys.Conditions;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;

import com.QuestSys.Events.EventTypes;

public class GoToo extends Condition {
    Location targetLocation;
    public GoToo(Location targetLocation) {
        super("go_too", EventTypes.PlayerMoveEvent, 1);
        this.targetLocation = targetLocation;
    }
    
    public static Condition create(Map<?, ?> values) {
	    // Location location = Location.deserialize(null);
        
    	if (values.get("position") instanceof String pos) {

		}else throw new IllegalArgumentException("position is null");

		double targetCount = (int)ParseTargetCount(values);
		// return new GoToo(location);
        return null;
	}

    @Override
    protected void onEvent(Event e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onEvent'");
    }

    @Override
    public Map<String, Object> GetData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetData'");
    }

    @Override
    public void SetData(Map<String, Object> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'SetData'");
    }
    
}
