/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.asset.model.impl;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.model.Group;
import com.liferay.portal.test.DeleteAfterTestRun;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.util.test.GroupTestUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetCategoryConstants;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.util.test.AssetTestUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author José Manuel Navarro
 */
@ExecutionTestListeners(listeners = {MainServletExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class AssetVocabularyImplTest {

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testHasMoreThanOneCategorySelected() throws Exception {
		AssetVocabulary vocabulary1 = AssetTestUtil.addVocabulary(
			_group.getGroupId(), 1, AssetCategoryConstants.ALL_CLASS_TYPE_IDS,
			true);

		AssetCategory category11 = AssetTestUtil.addCategory(
			_group.getGroupId(), vocabulary1.getVocabularyId());
		AssetCategory category12 = AssetTestUtil.addCategory(
			_group.getGroupId(), vocabulary1.getVocabularyId());

		Assert.assertFalse(
			vocabulary1.hasMoreThanOneCategorySelected(new long[0]));
		Assert.assertFalse(
			vocabulary1.hasMoreThanOneCategorySelected(
				new long[] {category11.getCategoryId()}));
		Assert.assertTrue(
			vocabulary1.hasMoreThanOneCategorySelected(
				new long[] {
					category11.getCategoryId(), category12.getCategoryId()
				}));

		AssetVocabulary vocabulary2 = AssetTestUtil.addVocabulary(
			_group.getGroupId(), 2, AssetCategoryConstants.ALL_CLASS_TYPE_IDS,
			true);

		AssetCategory category21 = AssetTestUtil.addCategory(
			_group.getGroupId(), vocabulary2.getVocabularyId());
		AssetCategory category22 = AssetTestUtil.addCategory(
			_group.getGroupId(), vocabulary2.getVocabularyId());

		Assert.assertFalse(
			vocabulary1.hasMoreThanOneCategorySelected(
				new long[] {
					category21.getCategoryId(), category22.getCategoryId()
				}));
		Assert.assertFalse(
			vocabulary2.hasMoreThanOneCategorySelected(new long[0]));
		Assert.assertFalse(
			vocabulary2.hasMoreThanOneCategorySelected(
				new long[] {category21.getCategoryId()}));
		Assert.assertTrue(
			vocabulary2.hasMoreThanOneCategorySelected(
				new long[] {
					category21.getCategoryId(), category22.getCategoryId()
				}));
	}

	@Test
	public void testIsAssociatedToClassNameId() throws Exception {
		AssetVocabulary vocabulary = AssetTestUtil.addVocabulary(
			_group.getGroupId(), AssetCategoryConstants.ALL_CLASS_NAME_IDS,
			AssetCategoryConstants.ALL_CLASS_TYPE_IDS, true);

		Assert.assertTrue(vocabulary.isAssociatedToClassNameId(1));

		vocabulary = AssetTestUtil.addVocabulary(
			_group.getGroupId(), 1, AssetCategoryConstants.ALL_CLASS_TYPE_IDS,
			true);

		Assert.assertTrue(vocabulary.isAssociatedToClassNameId(1));
		Assert.assertFalse(vocabulary.isAssociatedToClassNameId(2));
	}

	@Test
	public void testIsMissingRequiredCategory() throws Exception {
		AssetVocabulary vocabulary = AssetTestUtil.addVocabulary(
			_group.getGroupId(), 1, AssetCategoryConstants.ALL_CLASS_TYPE_IDS,
			false);

		AssetTestUtil.addCategory(
			_group.getGroupId(), vocabulary.getVocabularyId());

		Assert.assertFalse(
			vocabulary.isMissingRequiredCategory(1, new long[]{1}));

		vocabulary = AssetTestUtil.addVocabulary(
			_group.getGroupId(), 1, AssetCategoryConstants.ALL_CLASS_TYPE_IDS,
			true);

		Assert.assertTrue(
			vocabulary.isMissingRequiredCategory(1, new long[]{1}));
		Assert.assertFalse(
			vocabulary.isMissingRequiredCategory(2, new long[0]));

		AssetCategory category = AssetTestUtil.addCategory(
			_group.getGroupId(), vocabulary.getVocabularyId());

		Assert.assertTrue(
			vocabulary.isMissingRequiredCategory(1, new long[]{1}));
		Assert.assertFalse(
			vocabulary.isMissingRequiredCategory(
				1, new long[]{category.getCategoryId()}));
	}

	@Test
	public void testIsRequired() throws Exception {
		AssetVocabulary vocabulary = AssetTestUtil.addVocabulary(
			_group.getGroupId(), AssetCategoryConstants.ALL_CLASS_NAME_IDS,
			AssetCategoryConstants.ALL_CLASS_TYPE_IDS, false);

		Assert.assertFalse(vocabulary.isRequired(1));
		Assert.assertFalse(vocabulary.isRequired(2));

		vocabulary = AssetTestUtil.addVocabulary(
			_group.getGroupId(), AssetCategoryConstants.ALL_CLASS_NAME_IDS,
			AssetCategoryConstants.ALL_CLASS_TYPE_IDS, true);

		Assert.assertTrue(vocabulary.isRequired(1));
		Assert.assertTrue(vocabulary.isRequired(2));

		vocabulary = AssetTestUtil.addVocabulary(
			_group.getGroupId(), 1, AssetCategoryConstants.ALL_CLASS_TYPE_IDS,
			false);

		Assert.assertFalse(vocabulary.isRequired(1));
		Assert.assertFalse(vocabulary.isRequired(2));

		vocabulary = AssetTestUtil.addVocabulary(
			_group.getGroupId(), 1, AssetCategoryConstants.ALL_CLASS_TYPE_IDS,
			true);

		Assert.assertTrue(vocabulary.isRequired(1));
		Assert.assertFalse(vocabulary.isRequired(2));
	}

	@DeleteAfterTestRun
	private Group _group;

}