/*
 * DomUI Java User Interface library
 * Copyright (c) 2010 by Frits Jalvingh, Itris B.V.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * See the "sponsors" file for a list of supporters.
 *
 * The latest version of DomUI and related code, support and documentation
 * can be found at http://www.domui.org/
 * The contact for the project is Frits Jalvingh <jal@etc.to>.
 */
package to.etc.domui.component.meta;

import java.util.*;

import javax.annotation.*;

import to.etc.domui.component.input.*;
import to.etc.domui.component.lookup.*;
import to.etc.domui.component.meta.impl.*;
import to.etc.domui.util.*;
import to.etc.webapp.nls.*;
import to.etc.webapp.query.*;

public interface ClassMetaModel {
	/**
	 * FIXME Questionable nullity
	 * @return
	 */
	@Nonnull
	Class< ? > getActualClass();

	/**
	 * Returns the message bundle for translations related to this class. This will never return null.
	 * @return
	 */
	@Nonnull
	BundleRef getClassBundle();

	@Nonnull
	List<PropertyMetaModel< ? >> getProperties();

	/**
	 * Returns the named property on <i>this</i> class. This does not allow
	 * a property path (dotted names).
	 * @param name
	 * @return
	 */
	@Nullable
	PropertyMetaModel< ? > findSimpleProperty(String name);

	/**
	 * Returns a property reference to the specified property by following the dotted path
	 * starting at this class. This returns a synthetic PropertyMetaModel which has all of
	 * the values for the last part in the path (the actual property reached), but with
	 * accessors that reach that property by following all intermediary objects.
	 *
	 * @param name
	 * @return
	 */
	@Nullable
	PropertyMetaModel< ? > findProperty(String name);

	boolean isPersistentClass();

	/**
	 * If this is a persistent class that is directly mapped onto some table, this <i>might</i> return the table name. This
	 * should NOT return a name for data that is strictly derived from a metamodel-based database because there all
	 * value records share the same table.
	 * @return
	 */
	@Nullable
	String getTableName();

	/**
	 * Returns the property containing the primary key for this object, if one can be found.
	 * @return
	 */
	@Nullable
	PropertyMetaModel< ? > getPrimaryKey();

	/**
	 * If this class is an Enum or represents some enumerated value, this returns the possible value objects. If
	 * this is not a domain type this MUST return null.
	 * @return
	 */
	@Nullable
	Object[] getDomainValues();

	/**
	 * For a Domain type (Enum, Boolean) this returns a label for a given domain value, or null if no label is defined. When called for
	 * a non-domain type this will throw an exception.
	 * @param loc
	 * @param value
	 * @return
	 */
	@Nullable
	String getDomainLabel(Locale loc, Object value);

	/**
	 * Return a user-presentable entity name (singular) for this class. This defaults to the classname itself if unset.
	 */
	@Nonnull
	String getUserEntityName();

	/**
	 * Returns the name of this entity in user terms; the returned name is plural.
	 * @return
	 */
	@Nullable
	String getUserEntityNamePlural();

	/**
	 * If this class is the UP in a relation this specifies that it must
	 * be shown as a COMBOBOX containing choices. It contains a generator
	 * for the values to show. This is a default for all relations in which
	 * this class is the parent; it can be overridden in individual relations.
	 *
	 * @return
	 */
	@Nullable
	Class< ? extends IComboDataSet< ? >> getComboDataSet();

	/**
	 * When this relation-property is presented as a single field this can contain a class to render
	 * that field as a string.
	 * @return
	 */
	@Nullable
	Class< ? extends ILabelStringRenderer< ? >> getComboLabelRenderer();

	/**
	 * When set, the entire content of a combobox's OPTION tag can be
	 * generated by this content renderer. When set, the {@link #getComboDisplayProperties()},
	 * {@link #getComboLabelRenderer()} properties are ignored.
	 * @return
	 */
	@Nullable
	Class< ? extends INodeContentRenderer< ? >> getComboNodeRenderer();

	/**
	 * If this object is shown in a combobox it needs to show the following
	 * properties as the display value.
	 * @return
	 */
	@Nonnull
	List<DisplayPropertyMetaModel> getComboDisplayProperties();

	/**
	 * The property name to sort the combobox's presentation on.
	 * @return
	 */
	@Nullable
	String getComboSortProperty();

	/**
	 * If this object is shown in a Table it needs to show the following
	 * properties there. This value gets set from @MetaObject. These columns
	 * are also used when the table is shown as the result of a {@link LookupInput}
	 * search.
	 * @return
	 */
	@Nonnull
	List<DisplayPropertyMetaModel> getTableDisplayProperties();

	/**
	 * Returns the SORTED list of search properties defined on this class. These are
	 * used by default for all table based search operations like {@link LookupInput}
	 * and {@link LookupForm}. The value gets set from either the {@link MetaSearch} or
	 * the {@link MetaSearch} annotations.
	 * @return
	 */
	@Nonnull
	List<SearchPropertyMetaModel> getSearchProperties();

	/**
	 * Returns the SORTED list of key word search properties defined on this class. These
	 * are used by {@link LookupInput} to define the "search-as-I-type" functionality.
	 * The value gets set from either the {@link MetaSearch} or the {@link MetaSearch}
	 * annotations.
	 * @return
	 */
	@Nonnull
	List<SearchPropertyMetaModel> getKeyWordSearchProperties();

	/**
	 * Contains the name of the property to use for sorting initially. It is used to define
	 * the initial sort order when a table is shown for the 1st time. It can be null in which
	 * case the table query defines the sort order.
	 * @return
	 */
	@Nullable
	String getDefaultSortProperty();

	/**
	 * When a {@link #getDefaultSortProperty()} is defined, this defines the sort order to use
	 * initially.
	 * @return
	 */
	@Nonnull
	SortableType getDefaultSortDirection();

	/**
	 * When present this gives a hint to the component factories to help with choosing a
	 * proper component to <i>select</i> a single record of this type. This is only used
	 * when this class is the parent in an UP relation, and the child needs to add a
	 * control to help it select one parent.
	 * @return
	 */
	@Nullable
	String getComponentTypeHint();

	/**
	 * When this class is to be selected as a parent in an UP relation using an InputLookup
	 * control this describes the renderer to use to display the <i>currently selected</i>
	 * record in the edit page. If null this will use {@link #getLookupSelectedProperties()}.
	 * @return
	 */
	@Nullable
	Class< ? extends INodeContentRenderer< ? >> getLookupSelectedRenderer();

	/**
	 * When this class is to be selected as a parent in an UP relation using an InputLookup
	 * control this describes the properties to use to display the <i>currently selected</i>
	 * record in the edit page.
	 * @return
	 */
	@Nonnull
	List<DisplayPropertyMetaModel> getLookupSelectedProperties();

	/**
	 * If this is a persistent class, this should create a base QCriteria instance to do queries
	 * on this class. The QCriteria&lt;T&gt; instance returned <i>must</i> have a T that is equal
	 * to the value returned by this.getActualClass(). In addition it should have only restrictions
	 * that limit the result to valid instances of this class, <i>nothing else</i>! This usually
	 * means the restriction set is empty.
	 *
	 * @return
	 * @throws Exception
	 */
	@Nonnull
	QCriteria< ? > createCriteria() throws Exception;

	/**
	 * For Lookup and Combo fields, this can return a QueryManipulator instance that will alter the base
	 * query for the list-of-this to show.
	 * @return
	 */
	@Nullable
	IQueryManipulator< ? > getQueryManipulator();
}
