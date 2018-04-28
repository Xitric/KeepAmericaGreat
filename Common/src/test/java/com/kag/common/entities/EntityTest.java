package com.kag.common.entities;

import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.Collection;

class EntityTest {

	@Test
	void addPart() {
		//Arrange
		PartType positionType = PartType.getType(PositionPart.class);
		PartType bboxType = PartType.getType(BoundingBoxPart.class);

		Entity empty = new Entity();
		Entity addedSinglePart = new Entity();
		Entity addedDuplicateParts = new Entity();
		Entity addedMultipleParts = new Entity();

		//Act
		addedSinglePart.addPart(new PositionPart(0, 0));

		addedDuplicateParts.addPart(new PositionPart(0, 0));
		addedDuplicateParts.addPart(new PositionPart(0, 0));

		addedMultipleParts.addPart(new PositionPart(0, 0));
		addedMultipleParts.addPart(new BoundingBoxPart(0, 0));

		//Assert
		BitSet emptyBits = empty.getBits();
		Assertions.assertTrue(emptyBits.cardinality() == 0);

		BitSet addedSingleBits = addedSinglePart.getBits();
		Assertions.assertTrue(addedSingleBits.get(positionType.getId()));
		Assertions.assertTrue(addedSingleBits.cardinality() == 1);

		BitSet addedDuplicateBits = addedDuplicateParts.getBits();
		Assertions.assertTrue(addedDuplicateBits.get(positionType.getId()));
		Assertions.assertTrue(addedDuplicateBits.cardinality() == 1);

		BitSet addedMultipleBits = addedMultipleParts.getBits();
		Assertions.assertTrue(addedMultipleBits.get(positionType.getId()));
		Assertions.assertTrue(addedMultipleBits.get(bboxType.getId()));
		Assertions.assertTrue(addedMultipleBits.cardinality() == 2);
	}

	@Test
	void removePart() {
		//Arrange
		PartType positionType = PartType.getType(PositionPart.class);
		PartType bboxType = PartType.getType(BoundingBoxPart.class);

		PositionPart positionPart1 = new PositionPart(0, 0);
		PositionPart positionPart2 = new PositionPart(0, 0);
		BoundingBoxPart bboxPart = new BoundingBoxPart(0, 0);

		Entity empty = new Entity();
		empty.addPart(positionPart1);

		Entity removedSinglePart = new Entity();
		removedSinglePart.addPart(positionPart1);
		removedSinglePart.addPart(bboxPart);

		Entity removedSingleDuplicatePart = new Entity();
		removedSingleDuplicatePart.addPart(positionPart1);
		removedSingleDuplicatePart.addPart(positionPart2);
		removedSingleDuplicatePart.addPart(bboxPart);

		//Act
		empty.removePart(positionPart1);

		removedSinglePart.removePart(bboxPart);

		removedSingleDuplicatePart.removePart(positionPart2);

		//Assert
		BitSet emptyBits = empty.getBits();
		Assertions.assertTrue(emptyBits.cardinality() == 0);

		BitSet removedSingleBits = removedSinglePart.getBits();
		Assertions.assertTrue(removedSingleBits.get(positionType.getId()));
		Assertions.assertTrue(removedSingleBits.cardinality() == 1);

		BitSet removedSingleDuplicateBits = removedSingleDuplicatePart.getBits();
		Assertions.assertTrue(removedSingleDuplicateBits.get(positionType.getId()));
		Assertions.assertTrue(removedSingleDuplicateBits.get(bboxType.getId()));
		Assertions.assertTrue(removedSingleDuplicateBits.cardinality() == 2);
	}

	@Test
	void getPart() {
		//Arrange
		PositionPart positionPart1 = new PositionPart(0, 0);
		PositionPart positionPart2 = new PositionPart(0, 0);
		BoundingBoxPart bboxPart = new BoundingBoxPart(0, 0);

		Entity empty = new Entity();

		Entity singlePart = new Entity();
		singlePart.addPart(positionPart1);

		Entity multipleParts = new Entity();
		multipleParts.addPart(positionPart1);
		multipleParts.addPart(positionPart2);

		Entity multiplePartTypes = new Entity();
		multiplePartTypes.addPart(positionPart1);
		multiplePartTypes.addPart(positionPart2);
		multiplePartTypes.addPart(bboxPart);

		//Act and assert
		Assertions.assertNull(empty.getPart(PositionPart.class));

		Assertions.assertTrue(singlePart.getPart(PositionPart.class) == positionPart1);
		Assertions.assertNull(singlePart.getPart(BoundingBoxPart.class));

		Assertions.assertTrue(multipleParts.getPart(PositionPart.class) == positionPart1 ||
				multipleParts.getPart(PositionPart.class) == positionPart2);

		Assertions.assertTrue(multiplePartTypes.getPart(PositionPart.class) == positionPart1 ||
				multiplePartTypes.getPart(PositionPart.class) == positionPart2);
		Assertions.assertTrue(multiplePartTypes.getPart(BoundingBoxPart.class) == bboxPart);
	}

	@Test
	void getParts() {
		//Arrange
		PositionPart positionPart1 = new PositionPart(0, 0);
		PositionPart positionPart2 = new PositionPart(0, 0);
		BoundingBoxPart bboxPart = new BoundingBoxPart(0, 0);

		Entity empty = new Entity();

		Entity singlePart = new Entity();
		singlePart.addPart(positionPart1);

		Entity multipleParts = new Entity();
		multipleParts.addPart(positionPart1);
		multipleParts.addPart(positionPart2);

		Entity multiplePartTypes = new Entity();
		multiplePartTypes.addPart(positionPart1);
		multiplePartTypes.addPart(positionPart2);
		multiplePartTypes.addPart(bboxPart);

		//Act and assert
		Assertions.assertTrue(empty.getParts(PositionPart.class).isEmpty());

		Collection<PositionPart> singlePartContent = singlePart.getParts(PositionPart.class);
		Assertions.assertTrue(singlePartContent.size() == 1);
		Assertions.assertTrue(singlePartContent.contains(positionPart1));
		Assertions.assertTrue(singlePart.getParts(BoundingBoxPart.class).isEmpty());

		Collection<PositionPart> multiplePartsContent = multipleParts.getParts(PositionPart.class);
		Assertions.assertTrue(multiplePartsContent.size() == 2);
		Assertions.assertTrue(multiplePartsContent.contains(positionPart1));
		Assertions.assertTrue(multiplePartsContent.contains(positionPart2));

		Collection<PositionPart> multiplePartTypesContent1 = multiplePartTypes.getParts(PositionPart.class);
		Assertions.assertTrue(multiplePartTypesContent1.size() == 2);
		Assertions.assertTrue(multiplePartTypesContent1.contains(positionPart1));
		Assertions.assertTrue(multiplePartTypesContent1.contains(positionPart2));
		Collection<BoundingBoxPart> multiplePartTypesContent2 = multiplePartTypes.getParts(BoundingBoxPart.class);
		Assertions.assertTrue(multiplePartTypesContent2.size() == 1);
		Assertions.assertTrue(multiplePartTypesContent2.contains(bboxPart));
	}

	@Test
	void getPartsDeep() {
		//Arrange
		PositionPart positionPart = new PositionPart(0, 0);
		AbsolutePositionPart absolutePositionPart = new AbsolutePositionPart(0, 0);

		Entity empty = new Entity();

		Entity shallow = new Entity();
		shallow.addPart(positionPart);

		Entity deep = new Entity();
		deep.addPart(absolutePositionPart);

		Entity shallowAndDeep = new Entity();
		shallowAndDeep.addPart(positionPart);
		shallowAndDeep.addPart(absolutePositionPart);

		Entity multipleParts = new Entity();
		multipleParts.addPart(positionPart);
		multipleParts.addPart(new BoundingBoxPart(0, 0));

		//Act and Assert
		Assertions.assertTrue(empty.getPartsDeep(PositionPart.class).isEmpty());

		Collection<? extends PositionPart> shallowContent = shallow.getPartsDeep(PositionPart.class);
		Assertions.assertTrue(shallowContent.size() == 1);
		Assertions.assertTrue(shallowContent.contains(positionPart));

		Collection<? extends PositionPart> deepContent = deep.getPartsDeep(PositionPart.class);
		Assertions.assertTrue(deepContent.size() == 1);
		Assertions.assertTrue(deepContent.contains(absolutePositionPart));

		Collection<? extends PositionPart> shallowDeepContent = shallowAndDeep.getPartsDeep(PositionPart.class);
		Assertions.assertTrue(shallowDeepContent.size() == 2);
		Assertions.assertTrue(shallowDeepContent.contains(positionPart));
		Assertions.assertTrue(shallowDeepContent.contains(absolutePositionPart));
		Collection<? extends AbsolutePositionPart> deepOnlyContent = shallowAndDeep.getPartsDeep(AbsolutePositionPart.class);
		Assertions.assertTrue(deepOnlyContent.size() == 1);
		Assertions.assertTrue(deepOnlyContent.contains(absolutePositionPart));

		Collection<? extends PositionPart> multipleContent = multipleParts.getPartsDeep(PositionPart.class);
		Assertions.assertTrue(multipleContent.size() == 1);
		Assertions.assertTrue(multipleContent.contains(positionPart));
	}

	@Test
	void hasPart() {
		//Arrange
		PositionPart positionPart = new PositionPart(0, 0);

		Entity empty = new Entity();

		Entity removed = new Entity();
		removed.addPart(positionPart);
		removed.removePart(positionPart);

		Entity removedDuplicate = new Entity();
		removedDuplicate.addPart(positionPart);
		removedDuplicate.addPart(new PositionPart(0, 0));
		removedDuplicate.removePart(positionPart);

		Entity onePart = new Entity();
		onePart.addPart(positionPart);

		Entity multipleParts = new Entity();
		multipleParts.addPart(new PositionPart(0, 0));
		multipleParts.addPart(new BoundingBoxPart(0, 0));

		//Act and assert
		Assertions.assertFalse(empty.hasPart(PositionPart.class));
		Assertions.assertFalse(removed.hasPart(PositionPart.class));
		Assertions.assertTrue(removedDuplicate.hasPart(PositionPart.class));
		Assertions.assertTrue(onePart.hasPart(PositionPart.class));
		Assertions.assertTrue(multipleParts.hasPart(PositionPart.class));
		Assertions.assertTrue(multipleParts.hasPart(BoundingBoxPart.class));
	}

	@Test
	void getBits() {
		//Arrange
		PartType positionType = PartType.getType(PositionPart.class);
		PartType bboxType = PartType.getType(BoundingBoxPart.class);

		PositionPart positionPart = new PositionPart(0, 0);

		Entity empty = new Entity();

		Entity addedParts = new Entity();
		addedParts.addPart(new PositionPart(0, 0));
		addedParts.addPart(new BoundingBoxPart(0, 0));

		Entity addedDuplicates = new Entity();
		addedDuplicates.addPart(new PositionPart(0, 0));
		addedDuplicates.addPart(new PositionPart(0, 0));

		Entity removedPart = new Entity();
		removedPart.addPart(positionPart);
		removedPart.addPart(new BoundingBoxPart(0, 0));
		removedPart.removePart(positionPart);

		Entity removedPartDuplicate = new Entity();
		removedPartDuplicate.addPart(positionPart);
		removedPartDuplicate.addPart(new PositionPart(0, 0));
		removedPartDuplicate.removePart(positionPart);

		//Act and assert
		BitSet emptyBits = empty.getBits();
		Assertions.assertTrue(emptyBits.cardinality() == 0);

		BitSet addedBits = addedParts.getBits();
		Assertions.assertTrue(addedBits.cardinality() == 2);
		Assertions.assertTrue(addedBits.get(positionType.getId()));
		Assertions.assertTrue(addedBits.get(bboxType.getId()));

		BitSet addedDuplicateBits = addedDuplicates.getBits();
		Assertions.assertTrue(addedDuplicateBits.cardinality() == 1);
		Assertions.assertTrue(addedDuplicateBits.get(positionType.getId()));

		BitSet removedBits = removedPart.getBits();
		Assertions.assertTrue(removedBits.cardinality() == 1);
		Assertions.assertTrue(removedBits.get(bboxType.getId()));

		BitSet removedDuplicateBits = removedPartDuplicate.getBits();
		Assertions.assertTrue(removedDuplicateBits.cardinality() == 1);
		Assertions.assertTrue(removedDuplicateBits.get(positionType.getId()));
	}
}