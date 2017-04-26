/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.ticket.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class DateAdapter extends XmlAdapter<String, Date> {

    private final SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Date unmarshal(String v) throws Exception {
        return formatoData.parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        return formatoData.format(v);
    }

}
