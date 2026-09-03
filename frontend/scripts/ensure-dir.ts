import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// 자동 생성 모듈이 저장될 디렉터리 경로
const targetDir = path.resolve(__dirname, '../src/api/generated');

if (!fs.existsSync(targetDir)) {
  fs.mkdirSync(targetDir, { recursive: true });
  console.log(`📁 디렉터리 생성 완료: ${targetDir}`);
}
