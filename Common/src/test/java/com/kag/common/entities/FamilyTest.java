package com.kag.common.entities;

import com.kag.common.entities.parts.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Kasper
 */
class FamilyTest {

	@Test
	void forAll() {
		//Arrange
		Entity empty = new Entity();

		Entity fewerParts = new Entity();
		fewerParts.addPart(new PositionPart(0, 0));
		fewerParts.addPart(new BoundingBoxPart(0, 0));

		Entity matchingParts = new Entity();
		matchingParts.addPart(new PositionPart(0, 0));
		matchingParts.addPart(new BoundingBoxPart(0, 0));
		matchingParts.addPart(new BlockingPart());

		Entity moreParts = new Entity();
		moreParts.addPart(new PositionPart(0, 0));
		moreParts.addPart(new BoundingBoxPart(0, 0));
		moreParts.addPart(new BlockingPart());
		moreParts.addPart(new MovingPart(0));

		//Act
		Family family = Family.forAll(PositionPart.class, BlockingPart.class, BoundingBoxPart.class);

		//Assert
		Assertions.assertFalse(family.matches(empty.getBits()));
		Assertions.assertFalse(family.matches(fewerParts.getBits()));
		Assertions.assertTrue(family.matches(matchingParts.getBits()));
		Assertions.assertTrue(family.matches(moreParts.getBits()));
	}

	@Test
	void forAny() {
		//Arrange
		Entity empty = new Entity();

		Entity noMatchingParts = new Entity();
		noMatchingParts.addPart(new BlockingPart());

		Entity firstPartMatching = new Entity();
		firstPartMatching.addPart(new PositionPart(0, 0));

		Entity secondPartMatching = new Entity();
		secondPartMatching.addPart(new AbsolutePositionPart(0, 0));

		Entity bothPartsMatching = new Entity();
		bothPartsMatching.addPart(new PositionPart(0, 0));
		bothPartsMatching.addPart(new AbsolutePositionPart(0, 0));

		Entity moreParts = new Entity();
		moreParts.addPart(new PositionPart(0, 0));
		moreParts.addPart(new AbsolutePositionPart(0, 0));
		moreParts.addPart(new BlockingPart());

		//Act
		Family family = Family.forAny(PositionPart.class, AbsolutePositionPart.class);

		//Assert
		Assertions.assertFalse(family.matches(empty.getBits()));
		Assertions.assertFalse(family.matches(noMatchingParts.getBits()));
		Assertions.assertTrue(family.matches(firstPartMatching.getBits()));
		Assertions.assertTrue(family.matches(secondPartMatching.getBits()));
		Assertions.assertTrue(family.matches(bothPartsMatching.getBits()));
		Assertions.assertTrue(family.matches(moreParts.getBits()));
	}

	@Test
	void forNone() {
		//Arrange
		Entity empty = new Entity();

		Entity noMatches = new Entity();
		noMatches.addPart(new BlockingPart());

		Entity oneMatch = new Entity();
		oneMatch.addPart(new BlockingPart());
		oneMatch.addPart(new PositionPart(0, 0));

		Entity moreMatches = new Entity();
		moreMatches.addPart(new BlockingPart());
		moreMatches.addPart(new PositionPart(0, 0));
		moreMatches.addPart(new BoundingBoxPart(0, 0));

		//Act
		Family family = Family.forNone(PositionPart.class, BoundingBoxPart.class);

		//Assert
		Assertions.assertTrue(family.matches(empty.getBits()));
		Assertions.assertTrue(family.matches(noMatches.getBits()));
		Assertions.assertFalse(family.matches(oneMatch.getBits()));
		Assertions.assertFalse(family.matches(moreMatches.getBits()));
	}

	@Test
	void includingAll() {
		//Arrange
		Entity empty = new Entity();

		Entity fewerParts = new Entity();
		fewerParts.addPart(new PositionPart(0, 0));
		fewerParts.addPart(new BoundingBoxPart(0, 0));

		Entity matchingParts = new Entity();
		matchingParts.addPart(new PositionPart(0, 0));
		matchingParts.addPart(new BoundingBoxPart(0, 0));
		matchingParts.addPart(new BlockingPart());

		Entity moreParts = new Entity();
		moreParts.addPart(new PositionPart(0, 0));
		moreParts.addPart(new BoundingBoxPart(0, 0));
		moreParts.addPart(new BlockingPart());
		moreParts.addPart(new MovingPart(0));

		//Act
		Family family = Family.forAll(PositionPart.class).includingAll(BoundingBoxPart.class, BlockingPart.class);

		//Assert
		Assertions.assertFalse(family.matches(empty.getBits()));
		Assertions.assertFalse(family.matches(fewerParts.getBits()));
		Assertions.assertTrue(family.matches(matchingParts.getBits()));
		Assertions.assertTrue(family.matches(moreParts.getBits()));
	}

	@Test
	void includingAny() {
		//Arrange
		Entity empty = new Entity();

		Entity noMatchingParts = new Entity();
		noMatchingParts.addPart(new BlockingPart());

		Entity firstPartMatching = new Entity();
		firstPartMatching.addPart(new PositionPart(0, 0));

		Entity secondPartMatching = new Entity();
		secondPartMatching.addPart(new AbsolutePositionPart(0, 0));

		Entity thirdPartMatching = new Entity();
		thirdPartMatching.addPart(new BoundingBoxPart(0, 0));

		Entity allPartsMatching = new Entity();
		allPartsMatching.addPart(new PositionPart(0, 0));
		allPartsMatching.addPart(new AbsolutePositionPart(0, 0));
		allPartsMatching.addPart(new BoundingBoxPart(0, 0));

		Entity moreParts = new Entity();
		moreParts.addPart(new PositionPart(0, 0));
		moreParts.addPart(new AbsolutePositionPart(0, 0));
		moreParts.addPart(new BlockingPart());

		//Act
		Family family = Family.forAny(PositionPart.class).includingAny(AbsolutePositionPart.class, BoundingBoxPart.class);

		//Assert
		Assertions.assertFalse(family.matches(empty.getBits()));
		Assertions.assertFalse(family.matches(noMatchingParts.getBits()));
		Assertions.assertTrue(family.matches(firstPartMatching.getBits()));
		Assertions.assertTrue(family.matches(secondPartMatching.getBits()));
		Assertions.assertTrue(family.matches(thirdPartMatching.getBits()));
		Assertions.assertTrue(family.matches(allPartsMatching.getBits()));
		Assertions.assertTrue(family.matches(moreParts.getBits()));
	}

	@Test
	void excluding() {
		//Arrange
		Entity empty = new Entity();

		Entity noMatches = new Entity();
		noMatches.addPart(new BlockingPart());

		Entity oneMatch = new Entity();
		oneMatch.addPart(new BlockingPart());
		oneMatch.addPart(new PositionPart(0, 0));

		Entity moreMatches = new Entity();
		moreMatches.addPart(new BlockingPart());
		moreMatches.addPart(new PositionPart(0, 0));
		moreMatches.addPart(new AbsolutePositionPart(0, 0));
		moreMatches.addPart(new BoundingBoxPart(0, 0));

		//Act
		Family family = Family.forNone(PositionPart.class).excluding(AbsolutePositionPart.class, BoundingBoxPart.class);

		//Assert
		Assertions.assertTrue(family.matches(empty.getBits()));
		Assertions.assertTrue(family.matches(noMatches.getBits()));
		Assertions.assertFalse(family.matches(oneMatch.getBits()));
		Assertions.assertFalse(family.matches(moreMatches.getBits()));
	}

	@Test
	void matches() {
		//Arrange
		Entity empty = new Entity();

		Entity failsAll = new Entity();
		failsAll.addPart(new AbsolutePositionPart(0, 0));
		failsAll.addPart(new NamePart(""));

		Entity failsAny = new Entity();
		failsAny.addPart(new AbsolutePositionPart(0, 0));
		failsAny.addPart(new BlockingPart());

		Entity failsExclude = new Entity();
		failsExclude.addPart(new AbsolutePositionPart(0, 0));
		failsExclude.addPart(new BlockingPart());
		failsExclude.addPart(new BoundingBoxPart(0, 0));
		failsExclude.addPart(new MovingPart(0));

		Entity matching = new Entity();
		matching.addPart(new AbsolutePositionPart(0, 0));
		matching.addPart(new BlockingPart());
		matching.addPart(new BoundingBoxPart(0, 0));

		//Act
		//All: AbsolutePositionPart, BlockingPart
		//Any: BoundingBoxPart, NamePart
		//Exclude: MovingPart
		Family family = Family.forAll(AbsolutePositionPart.class)
				.includingAll(BlockingPart.class)
				.includingAny(BoundingBoxPart.class)
				.excluding(MovingPart.class)
				.includingAny(NamePart.class);

		//Assert
		Assertions.assertFalse(family.matches(empty.getBits()));
		Assertions.assertFalse(family.matches(failsAll.getBits()));
		Assertions.assertFalse(family.matches(failsAny.getBits()));
		Assertions.assertFalse(family.matches(failsExclude.getBits()));
		Assertions.assertTrue(family.matches(matching.getBits()));
	}

	@Test
	void matchesInterleaved() {
		//Arrange
		Entity empty = new Entity();

		Entity failsAllInterleavesAny = new Entity();
		failsAllInterleavesAny.addPart(new AbsolutePositionPart(0, 0));
		failsAllInterleavesAny.addPart(new NamePart(""));

		Entity failsAnyAndAll = new Entity();
		failsAnyAndAll.addPart(new BlockingPart());

		Entity matchingOne = new Entity();
		matchingOne.addPart(new AbsolutePositionPart(0, 0));
		matchingOne.addPart(new BlockingPart());

		Entity matchingTwo = new Entity();
		matchingTwo.addPart(new AbsolutePositionPart(0, 0));
		matchingTwo.addPart(new BlockingPart());
		matchingTwo.addPart(new BoundingBoxPart(0, 0));

		//Act
		//All: AbsolutePositionPart, BlockingPart
		//Any: AbsolutePositionPart, BoundingBoxPart, NamePart
		//Exclude: MovingPart
		Family family = Family.forAll(AbsolutePositionPart.class)
				.includingAll(BlockingPart.class)
				.includingAny(AbsolutePositionPart.class, BoundingBoxPart.class)
				.excluding(MovingPart.class)
				.includingAny(NamePart.class);

		//Assert
		Assertions.assertFalse(family.matches(empty.getBits()));
		Assertions.assertFalse(family.matches(failsAllInterleavesAny.getBits()));
		Assertions.assertFalse(family.matches(failsAnyAndAll.getBits()));
		Assertions.assertTrue(family.matches(matchingOne.getBits()));
		Assertions.assertTrue(family.matches(matchingTwo.getBits()));
	}
}