/*
 * Copyright 2004-2007 Gary Bentley 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.josql.contrib;

import java.util.List;
import java.util.ArrayList;
import java.util.SortedMap;

import javax.swing.table.TableModel;

import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.Query;
import org.josql.QueryResults;

import org.josql.internal.Utilities;

import org.josql.expressions.SelectItemExpression;

/**
 * A table model suitable for use with Swing JTable.
 *
 * This is basically just an extension to {@link Query} that allows the
 * results to be iterated over, thereby providing the ability for objects to be reported on
 * that are held in memory.
 * <p>
 * One limitation here is that the SQL query must return columns rather than the objects
 * since the values need to be mapped by the renderer and editor.  For example:
 * <pre>
 *   SELECT lastModified,
 *          name
 *   FROM   java.io.File
 *   WHERE  name LIKE '%.html'
 * </pre>
 * <p>
 * This query would work but it should be noted that the select "columns" (since they do not have 
 * aliases assigned) will be labeled 1, 2, X and so on.
 * You can assign aliases to the "columns" and then use them in the report definition file.
 */  
public class JoSQLSwingTableModel extends Query implements TableModel
{

    private QueryResults results = null;
    private List listeners = new ArrayList ();

    public JoSQLSwingTableModel ()
    {


    }

    /**
     * Parse the SQL.  Note: this will cause a TableModelEvent to be fired to all
     * registered listeners indicating that the table header has changed.
     *
     * @param sql The SQL.
     * @throws QueryParseException If the sql cannot be parsed or if the query will not return 
     *                             columns.
     */
    public void parse (String sql)
	               throws QueryParseException
    {

	this.results = null;

	super.parse (sql);

	if (this.isWantObjects ())
	{

	    throw new QueryParseException ("Only SQL statements that return columns (not the objects passed in) can be used.");

	}

	this.notifyListeners (new TableModelEvent (this,
						   TableModelEvent.HEADER_ROW));

    }

    private void notifyListeners (TableModelEvent ev)
    {

	for (int i = 0; i < this.listeners.size (); i++)
	{

	    TableModelListener l = (TableModelListener) this.listeners.get (i);

	    l.tableChanged (ev);

	}

    }

    /**
     * Re-order the columns according to the column indices provided in <b>dirs</b>.
     * 
     * @param objs The objects to reorder.
     * @param dirs The columns to order by.
     * @return The results.
     * @throws QueryExecutionException If something goes wrong during execution of the
     *                                 query.
     * @throws QueryParseException If the column indices are out of range for the statement.
     * @see Query#reorder(List,SortedMap)
     */
    public QueryResults reorder (List      objs,
				 SortedMap dirs)
	                         throws    QueryExecutionException,
	                                   QueryParseException
    {

	// Get the order bys.
	this.results = super.reorder (objs,
				      dirs);

	// Notify the listeners that the data has changed.
	this.notifyListeners (new TableModelEvent (this));	

	return this.results;

    }

    /**
     * Re-order the columns according to the string representation provided by <b>orderBys</b>.
     * 
     * @param objs The objects to reorder.
     * @param orderBys The columns to order by.
     * @return The results.
     * @throws QueryExecutionException If something goes wrong during execution of the
     *                                 query.
     * @throws QueryParseException If the column indices are out of range for the statement.
     * @see Query#reorder(List,String)
     */
    public QueryResults reorder (List   objs,
				 String orderBys)
	                         throws QueryParseException,
					QueryExecutionException
    {

	this.results = super.reorder (objs,
				      orderBys);

	// Notify the listeners that the data has changed.
	this.notifyListeners (new TableModelEvent (this));	

	return this.results;

    }

    /**
     * Exectute the query and return the results.  A reference to the results is also held to 
     * allow them to be iterated over.  Note: this will cause a TableModelEvent to be fired to all
     * registered listeners indicating that ALL the table data has changed.
     *
     * @param l The List of objects to execute the query on.
     * @return The results.
     * @throws QueryExecutionException If the query cannot be executed, or if the query
     *                                 is set to return objects rather than "columns".
     */
    public QueryResults execute (List   l)
	                         throws QueryExecutionException
    {

	this.results = super.execute (l);

	// Notify the listeners that the data has changed.
	this.notifyListeners (new TableModelEvent (this));

	return this.results;

    }

    /**
     * Get any results, will be null unless {@link #execute(List)} has been called.
     *
     * @return The results.
     */
    public QueryResults getResults ()
    {

	return this.results;

    }

    /**
     * Clear any results.
     */
    public void clearResults ()
    {

	this.results = null;

    }

    /**
     * Get the name of the column, if the query has not yet been parsed then <code>null</code> is returned,
     * if the column does not have an alias then "ind + 1" is returned.
     *
     * @return The column name.
     */
    public String getColumnName (int ind)
    {

	List cs = this.getColumns ();

	if ((cs == null)
	    ||
	    (ind > (cs.size () - 1))
	   )
	{

	    return null;

	}

	SelectItemExpression s = (SelectItemExpression) cs.get (ind);

	String al = s.getAlias ();

	if (al == null)
	{

	    return (ind + 1) + "";

	}

	return al;

    }

    /**
     * The expected class of the object at column <b>i</b>.
     *
     * @return The class of the column.
     */
    public Class getColumnClass (int i)
    {

	List cs = this.getColumns ();

	if ((cs == null)
	    ||
	    (i > (cs.size () - 1))
	   )
	{

	    return null;

	}

	SelectItemExpression s = (SelectItemExpression) cs.get (i);

	try
	{

	    return Utilities.getObjectClass (s.getExpectedReturnType (this));

	} catch (Exception e) {

	    // Painful, but not much we can do.
	    return null;

	}

    }

    /**
     * Get the object at row <b>r</b>, column <b>c</b>.
     *
     * @param r The row.
     * @param c The column.
     * @return The object at that location.
     */
    public Object getValueAt (int r,
			      int c)
    {

	if ((this.results == null)
	    ||
	    (r > (this.results.getResults ().size () - 1))
	   )
	{

	    return null;

	}

	Object o = this.results.getResults ().get (r);

	if (o instanceof List)
	{

	    List l = (List) o;

	    if (c > (l.size () - 1))
	    {

		return null;

	    }

	    return l.get (c);

	} 

	if (c > 0)
	{

	    return null;

	}

	return o;

    }

    /**
     * Not supported, always throws a: {@link UnsupportedOperationException}.
     *
     * @param v The object to set at the location.
     * @param r The row.
     * @param c The column.
     * @throws UnsupportedOperationException Not supported.
     */ 
    public void setValueAt (Object v,
			    int    r,
			    int    c)
			    throws UnsupportedOperationException
    {

	// Do nothing for now...
	throw new UnsupportedOperationException ("This method not supported for: " +
						 this.getClass ().getName ());

    }

    /**
     * Cells are not editable since we do not store the results separately.
     *
     * @param r The row.
     * @param c The columns.
     * @return Always returns <code>false</code>.
     */
    public boolean isCellEditable (int r,
				   int c)
    {

	// Not sure what's best here... for now make them non-editable.
	return false;

    }

    /**
     * Number of rows.
     *
     * @return The row count.
     */
    public int getRowCount ()
    {

	if (this.results == null)
	{

	    return 0;

	}

	return this.results.getResults ().size ();

    }

    /**
     * Get the number of columns.
     *
     * @return The column count, returns 0 if the query has not yet been parsed.
     */
    public int getColumnCount ()
    {

	// See if we have any columns.
	if (this.getColumns () == null)
	{

	    return 0;

	}

	return this.getColumns ().size ();

    }

    public void removeTableModelListener (TableModelListener l)
    {

	this.listeners.remove (l);

    }

    public void addTableModelListener (TableModelListener l)
    {
	
	if (this.listeners.contains (l))
	{

	    return;

	}

	this.listeners.add (l);

    }

}
