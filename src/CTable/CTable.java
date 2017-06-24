package CTable;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
public class CTable extends JTable {
 public CMap map;
 public CTable(CMap cmp, TableModel tbl) {
  super(tbl);
  map = cmp;
  setUI(new CTUI());
 }
 public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
  // required because getCellRect is used in JTable constructor
  if (map == null)
   return super.getCellRect(row, column, includeSpacing);
  // add widths of all spanned logical cells
  int sk = map.visibleCell(row, column);
  //Rectangle r1 = super.getCellRect(row, sk, includeSpacing);
  Rectangle r1 = super.getCellRect( sk, column, includeSpacing);
  if (map.span( sk, column ) != 1)
   for (int i = 1; i < map.span( sk, column ); i++) {
    //r1.width += getColumnModel().getColumn(sk + i).getWidth();
    r1.height += this.getRowHeight( sk + i );
   }
  return r1;
 }
 public int rowAtPoint(Point p) {
  int x = super.columnAtPoint(p);
  // -1 is returned by columnAtPoint if the point is not in the table
  if (x < 0)
   return x;
  int y = super.rowAtPoint(p);
  return map.visibleCell(y, x);
 }
}