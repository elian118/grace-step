import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// 자동 생성 모듈이 저장될 디렉터리 경로
const targetDir = path.resolve(__dirname, '../src/api/generated');

if (fs.existsSync(targetDir)) {
  // 폴더 내부의 기존 파일들을 모두 삭제
  fs.readdirSync(targetDir).forEach((file) => {
    const filePath = path.join(targetDir, file);
    if (fs.statSync(filePath).isFile()) {
      fs.unlinkSync(filePath);
    }
  });
  console.log('🧹 기존 파일 청소 완료 ./src/api/generated directory');
} else {
  // 폴더가 없으면 새로 생성
  fs.mkdirSync(targetDir, { recursive: true });
  console.log('📁 폴더 생성 완료 ./src/api/generated directory');
}
