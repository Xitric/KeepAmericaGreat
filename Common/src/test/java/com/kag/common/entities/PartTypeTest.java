package com.kag.common.entities;

import com.kag.common.entities.parts.BlockingPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Kasper
 */
class PartTypeTest {

	@Test
	void getType() {
		//Arrange and act
		PartType positionType = PartType.getType(PositionPart.class);
		PartType bboxType = PartType.getType(BoundingBoxPart.class);
		PartType blockingType = PartType.getType(BlockingPart.class);

		PartType positionTypeRepeat = PartType.getType(PositionPart.class);
		PartType bboxTypeRepeat = PartType.getType(BoundingBoxPart.class);
		PartType blockingTypeRepeat = PartType.getType(BlockingPart.class);

		//Assert
		Assertions.assertTrue(positionType.getId() == 0);
		Assertions.assertTrue(bboxType.getId() == 1);
		Assertions.assertTrue(blockingType.getId() == 2);

		Assertions.assertTrue(positionType == positionTypeRepeat);
		Assertions.assertTrue(bboxType == bboxTypeRepeat);
		Assertions.assertTrue(blockingType == blockingTypeRepeat);
	}
}