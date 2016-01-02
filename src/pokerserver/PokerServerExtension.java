/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerserver;

import com.shephertz.app42.server.idomain.BaseServerAdaptor;
import com.shephertz.app42.server.idomain.IZone;

/**
 * @author Chirag
 */
public class PokerServerExtension extends BaseServerAdaptor{
    
    @Override
    public void onZoneCreated(IZone zone)
    {             
    	System.out.println();
    	System.out.print("Poker Server Extension : "+zone.getName());
        zone.setAdaptor(new PokerZoneExtension(zone));
    }
    
}
