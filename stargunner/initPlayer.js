importClass(Packages.games.caravan.character.FighterJet);
importClass(Packages.graphicslib3D.Point3D);
       
	var p1 = new FighterJet(new Point3D(0,10,-18));
	p1.scale(0.3,0.3,0.3);
	p1.setSpeed(0.01);
	p1.setFireRate(1000);
	