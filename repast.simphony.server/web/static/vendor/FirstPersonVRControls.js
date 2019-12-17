/**
 * @author brianpeiris / http://brian.peiris.io/
 *
 * Based on code from THREE.FirstPersonControls
 * @author mrdoob / http://mrdoob.com/
 * @author alteredq / http://alteredqualia.com/
 * @author paulirish / http://paulirish.com/
 */
//import * as THREE from 'three';

const ZAXIS = new THREE.Vector3(0, 0, 1);
const YAXIS = new THREE.Vector3(0, 1, 0);

export class FirstPersonVRControls {
  enabled = true;
  verticalMovement = false;
  strafing = false;
  boost = true;
  movementSpeed = 100.0;
  snapAngle = 1 * Math.PI / 180;
  boostFactor = 10;

  _angleQuaternion = new THREE.Quaternion();

  _moveForward = false;
  _moveBackward = false;
  _moveLeft = false;
  _moveRight = false;
  _moveUp = false;
  _moveDown = false;
  _boosting = false;

  _snapLeft = false;
  _snapRight = false;
  
  constructor (camera, scene, rig, world) {
    this._camera = camera;
    if (!rig) {
      this.rig = new THREE.Object3D();
      this.rig.add(camera);
      scene.add(this.rig);
    } else {
      this.rig = rig;
    }
    this.world = world;
  
    window.addEventListener('keydown', this._onKeyDown, false);
    window.addEventListener('keyup', this._onKeyUp, false);
  }

  _onKeyDown = (event) => {
    if (event.repeat) { return; }
 
    switch (event.key.toLowerCase()) {
      case 'arrowup': 
      case 'w': this._moveForward = true; break;

      case 'arrowleft': 
      case 'a': this._moveLeft = true; break;

      case 'arrowdown': 
      case 's': this._moveBackward = true; break;

      case 'arrowright':
      case 'd': this._moveRight = true; break;

      case 'r': this._moveUp = true; break;
      case 'f': this._moveDown = true; break;

      case 'shift': this._boosting = true; break;

//      case 'q': this.snap('left'); break;
//      case 'e': this.snap('right'); break;
      
      case 'q': this._snapLeft = true; break;

      case 'e': this._snapRight = true; break;
    }
  };

  _onKeyUp = (event) => {
    switch (event.key.toLowerCase()) {
      case 'arrowup': 
      case 'w': this._moveForward = false; break;

      case 'arrowleft': 
      case 'a': this._moveLeft = false; break;

      case 'arrowdown': 
      case 's': this._moveBackward = false; break;

      case 'arrowright':
      case 'd': this._moveRight = false; break;

      case 'r': this._moveUp = false; break;
      case 'f': this._moveDown = false; break;

      case 'shift': this._boosting = false; break;
      
      case 'q': this._snapLeft = false; break;
      
      case 'e': this._snapRight = false; break;
    }
  };

  _angle = 0;
  _tempMatrix = new THREE.Matrix4()
  snap (direction) {
    const deltaAngle = this.snapAngle * (direction === 'left' ? 1 : -1);
    this._angle += deltaAngle;
    this._angleQuaternion.setFromAxisAngle(YAXIS, this._angle);

    this._tempMatrix.makeTranslation(this._camera.position.x, this._camera.position.y, this._camera.position.z);
    this.rig.matrix.multiply(this._tempMatrix);

    this._tempMatrix.makeRotationY(-deltaAngle);
    this.rig.matrix.multiply(this._tempMatrix);

    this._tempMatrix.makeRotationY(deltaAngle * 2);
    this.rig.matrix.multiply(this._tempMatrix);

    this._tempMatrix.makeTranslation(-this._camera.position.x, -this._camera.position.y, -this._camera.position.z);
    this.rig.matrix.multiply(this._tempMatrix);

    this.rig.matrix.decompose(this.rig.position, this.rig.quaternion, this.rig.scale);
  }

  _direction = new THREE.Vector3();
  _collapseYComponent (quaternion) {
    this._direction.set(0, 0, 1);
    this._direction.applyQuaternion(quaternion);
    this._direction.y = 0;
    this._direction.normalize();
    quaternion.setFromUnitVectors(ZAXIS, this._direction);
  }

  _tempObject = new THREE.Object3D();
  update (delta) {
    if (!this.enabled) return;

    this._tempObject.position.set(0, 0, 0);
    this._tempObject.quaternion.copy(this._camera.quaternion);
    this._tempObject.quaternion.multiplyQuaternions(this._angleQuaternion, this._camera.quaternion);    
    this._collapseYComponent(this._tempObject.quaternion);
    
    let actualMoveSpeed = delta * this.movementSpeed;
    if (this.boost && this._boosting) actualMoveSpeed = actualMoveSpeed * this.boostFactor;
    
    if (this._moveForward) this._tempObject.translateZ(- actualMoveSpeed);
    if (this._moveBackward) this._tempObject.translateZ(actualMoveSpeed);

    if (this.strafing && this._moveLeft) this._tempObject.translateX(- actualMoveSpeed);
    if (this.strafing && this._moveRight) this._tempObject.translateX(actualMoveSpeed);

    if (this.verticalMovement && this._moveUp) this._tempObject.translateY(actualMoveSpeed);
    if (this.verticalMovement && this._moveDown) this._tempObject.translateY(- actualMoveSpeed);

    if (this._snapLeft) this.snap('left');
    if (this._snapRight) this.snap('right');
    
    this.rig.position.add(this._tempObject.position);
    
//    this.world._onControlsMoveEnd(this.rig.position);
  }

  dispose () {
    window.removeEventListener('keydown', this._onKeyDown, false);
    window.removeEventListener('keyup', this._onKeyUp, false);
  }
}
